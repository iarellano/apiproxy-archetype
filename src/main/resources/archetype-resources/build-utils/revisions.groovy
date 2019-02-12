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


def organization =  project.getProperties().get("apigee.org") as String
def environment = project.getProperties().get("apigee.env") as String
def api = project.getProperties().get("apiproxy.name") as String
def mangmntUrl = (project.getProperties().get("apigee.hosturl") + "/"
                    + project.getProperties().get("apigee.apiversion")) as String
def username = project.getProperties().get("apigee.username") as String
def password = project.getProperties().get("apigee.password") as String
def properties = new Properties()

def authorization = "Basic " + Base64.encoder.encodeToString((username + ":" + password).getBytes("utf-8")) as String

Properties requestProperties = new Properties()
requestProperties.setProperty("Authorization", authorization)
requestProperties.setProperty("Accept", "application/json")

def path = String.format("/organizations/%s/apis/%s", organization, api)
def request = new Request(mangmntUrl + path, requestProperties, "GET")

if (request.getStatusCode() == 200) { // If api exists
    def slurper = new JsonSlurper()
    def result = slurper.parseText(request.getContent())
    int versionsCount = result.revision.size()
    if (versionsCount > 0) { // If api has revisions
        def greatestRevision = result.revision.get(versionsCount - 1)
        path = String.format("/organizations/%s/environments/%s/apis/%s/deployments", organization, environment, api)
        request = new Request(mangmntUrl + path, requestProperties, "GET")

        if (request.getStatusCode() == 200) { // If api deployed
            result = slurper.parseText(request.getContent())
            def deployedRevision = result.revision.get(0).name
            properties.setProperty("apiproxy.revision.undeploying", deployedRevision)
        }
        properties.setProperty("apiproxy.revision", String.valueOf(Integer.parseInt(greatestRevision) + 1))
    } else { // api has no revisions
        properties.setProperty("apiproxy.revision", "1")
    }
} else if (request.getStatusCode() == 404) { // api does not exist
    properties.setProperty("apiproxy.revision", "1")
}

project.getProperties().putAll(properties)
File outputDirectory = new File("target/" + project.getProperties().get("apiproxy.name"))
outputDirectory.mkdirs()
PrintWriter pw = new PrintWriter(new File(outputDirectory, "revisions.properties"))
properties.list(pw)
pw.println("# apiproxy.revision is just an alias for apiproxy.revision.deploying")
pw.close()

log.info("apiproxy.revision.undeploying: " + properties.getProperty("apiproxy.revision.undeploying") != null
        ? String.format("No revision deployed in %s environment", project.getProperties().get("apigee.env"))
        : properties.getProperty("apiproxy.revision.undeploying"))

log.info("Next ApiProxy revision: " + properties.getProperty("apiproxy.revision"))
