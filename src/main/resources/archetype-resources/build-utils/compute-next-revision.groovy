import groovy.json.JsonSlurper

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.X509Certificate;

class Request {

    static {
        installSslSkipVerification()
    }

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

    private static void installSslSkipVerification() {

        X509TrustManager trustManager = createHollowTrustManager()
        TrustManager[] trustManagers = new TrustManager[1]
        trustManagers[0] = trustManager
        SSLContext sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustManagers, new SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory())
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    }

    private static X509TrustManager createHollowTrustManager() {
        return new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }
        };
    }
}


def organization =  project.getProperties().get("apigee.org") as String
def environment = project.getProperties().get("apigee.env") as String
def api = (project.getProperties().get("apiproxy.name") + project.getProperties().get("deployment.suffix")) as String
def mangmntUrl = project.getProperties().get("apigee.managementUrl") as String
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

def canonicalName = String.format("%s%s-rev%s",
        project.getProperties().get("apiproxy.name"),
        project.getProperties().get("deployment.suffix"),
        properties.getProperty("apiproxy.revision"))

properties.setProperty("apiproxy.canonicalName", canonicalName)

project.getProperties().putAll(properties)
File outputDirectory = new File(project.getProperties().get("apiproxy.basedir"))
outputDirectory.mkdirs()

PrintWriter pw = new PrintWriter(new File(outputDirectory, "computed-revision.properties"))
pw.println("# autogenerated properties")
properties.list(pw)
pw.close()

log.info("apiproxy.revision.undeploying: " + properties.getProperty("apiproxy.revision.undeploying") != null
        ? String.format("No revision deployed in %s environment", project.getProperties().get("apigee.env"))
        : properties.getProperty("apiproxy.revision.undeploying"))

log.info("Next ApiProxy revision: " + properties.getProperty("apiproxy.revision"))