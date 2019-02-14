import groovy.json.JsonSlurper;

class Request {

    private HttpURLConnection connection

    Request(String url, Properties properties, String method) {
        def targetUrl = new URL(url) as URL
        connection = targetUrl.openConnection(/*proxy*/) as HttpURLConnection
        connection.setRequestMethod(method)
        properties.each{ key, value ->  connection.setRequestProperty(key, value) }
    }

    int getStatusCode() {
        return connection.getResponseCode()
    }

    String getContent() {
        InputStream is = connection.getInputStream()
        byte[] buffer = new byte[1024]
        int bytesRead = 0
        ByteArrayOutputStream os = new ByteArrayOutputStream()
        while ((bytesRead = is.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, bytesRead)
        }
        return os.toString("UTF-8")
    }
}

def deployProfileActivated = project.getProperties().get("deployProfileActivated")
if ("true".equals(deployProfileActivated)) {
    log.info("Skipping getting deployed revision since deploy profile is active. See property deployProfileActivated")
    return
}

def organization =  project.getProperties().get("apigee.org") as String
def environment = project.getProperties().get("apigee.env") as String
def api = (project.getProperties().get("apiproxy.name") + project.getProperties().get("deployment.suffix")) as String
def mangmntUrl = project.getProperties().get("apigee.managementUrl") as String
def username = project.getProperties().get("apigee.username") as String
def password = project.getProperties().get("apigee.password") as String

def authorization = "Basic " + Base64.encoder.encodeToString((username + ":" + password).getBytes("utf-8")) as String

Properties requestProperties = new Properties()
requestProperties.setProperty("Authorization", authorization)
requestProperties.setProperty("Accept", "application/json")

def path = String.format("/organizations/%s/apis/%s", organization, api)
def request = new Request(mangmntUrl + path, requestProperties, "GET")
def apiproxyRevision = "true".equals( project.getProperties().get("integrationProfileActivated") )
        ? null
        : java.lang.System.properties.containsKey("apiproxy.revision")
            ? java.lang.System.properties.get("apiproxy.revision")
            : project.getProperties().get("apiproxy.revision")

if (request.getStatusCode() == 200) { // If api exists
    def slurper = new JsonSlurper()
    def result = slurper.parseText(request.getContent())
    int versionsCount = result.revision.size()
    if (versionsCount > 0) { // If api has revisions
        path = String.format("/organizations/%s/environments/%s/apis/%s/deployments", organization, environment, api)
        request = new Request(mangmntUrl + path, requestProperties, "GET")
        if (request.getStatusCode() == 200) { // If api deployed
            result = slurper.parseText(request.getContent())
            apiproxyRevision = result.revision.get(0).name
        }
    }
}

//project.getProperties().list(System.out)

if ( apiproxyRevision == null && "true".equals( project.getProperties().get("integrationProfileActivated") ) ) {
    throw new IllegalStateException("There is no deployed revision of API Proxy '" + api + "' on environment '" + environment + "' to run integration testing.")
} else {
    def canonicalName = String.format("%s%s-rev%s",
            project.getProperties().get("apiproxy.name"),
            project.getProperties().get("deployment.suffix"),
            apiproxyRevision)

    try {
        project.getProperties().setProperty("apiproxy.revision", apiproxyRevision)
        project.getProperties().setProperty("apiproxy.canonicalName", canonicalName)
        File outputDirectory = new File(project.getProperties().get("apiproxy.basedir"))
        outputDirectory.mkdirs()
    } catch (Exception e) {
        e.printStackTrace(System.err)
//        throw new IllegalStateException("Perhaps you may need to provide property apiproxy.revision i.e. -Dapiproxy.revision=1", e)
        throw e
    }
}