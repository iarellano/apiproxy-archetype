import io.swagger.v3.oas.models.Operation
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.oas.models.OpenAPI
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
@Grapes([
    @Grab(group='org.openapitools', module='openapi-generator', version='4.2.1'),
    @Grab(group='com.fasterxml.jackson.core', module='jackson-databind', version='2.9.8'),
    @Grab(group='com.fasterxml.jackson.dataformat', module='jackson-dataformat-yaml', version='2.9.8'),
    @Grab(group='com.fasterxml.jackson.datatype', module='jackson-datatype-jsr310', version='2.9.8'),
//    @Grab(group='io.swagger.parser.v3', module='swagger-parser', version='2.0.16'),
    @Grab(group='org.apache.commons', module='commons-lang3', version='3.4')
])

import java.nio.file.Files
import java.nio.file.StandardCopyOption

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper

import org.openapitools.codegen.CliOption
import org.openapitools.codegen.ClientOptInput
import org.openapitools.codegen.CodegenConfig
import org.openapitools.codegen.CodegenConstants
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.auth.AuthParser
import org.openapitools.codegen.config.CodegenConfigurator
import org.openapitools.codegen.config.GlobalSettings

def readDocument(def xmlFile) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance()
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder()
    Document doc = dBuilder.parse(xmlFile)
    doc.getDocumentElement().normalize()
    return doc
}

def writeDocument(def doc, def xmlFile) {
    doc.getDocumentElement().normalize()
    TransformerFactory transformerFactory = TransformerFactory.newInstance()
    Transformer transformer = transformerFactory.newTransformer()
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
    DOMSource source = new DOMSource(doc)
    StreamResult result = new StreamResult(xmlFile)
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.transform(source, result)
}

def apigeeCondition(def path, def method) {
    return String.format('(proxy.pathsuffix MatchesPath "%1$s") and (request.verb = "%2$s")',
            path.replaceAll("\\{.*?\\}", "*"), method.toUpperCase()
    )
}

def addFlowCondition(Document doc, String path, String method, String operationId, String summary) {
    Element desc = doc.createElement("Description")
    desc.setTextContent(summary)
    Element condition = doc.createElement("Condition")
    condition.setTextContent(apigeeCondition(path, method))
    Element flow = doc.createElement("Flow")
    flow.setAttribute("name", operationId)
    flow.appendChild(desc)
    flow.appendChild(doc.createElement("Request"))
    flow.appendChild(doc.createElement("Response"))
    flow.appendChild(condition)
    doc.getElementsByTagName("Flows").item(0).appendChild(flow)
}

def generateMockEndpoint(def spec) {
    CodegenConfigurator configurator = new  CodegenConfigurator()
    configurator.setInputSpec(spec)
    configurator.setGenerateAliasAsModel(true)
    configurator.setGeneratorName("nodejs-express-server")
    configurator.setVerbose(true)
    configurator.setLogToStderr(true)
    configurator.setOutputDir(new File(request.getOutputDirectory() + File.separator + "mock").getAbsolutePath())
    configurator.setValidateSpec(false)
    GlobalSettings.clearProperty(CodegenConstants.MODELS)
    final ClientOptInput input = configurator.toClientOptInput()
    final CodegenConfig config = input.getConfig()
    new DefaultGenerator().opts(input).generate()
}

def readSpec(def specLocation) {
    def specUrl = specLocation.matches("^(https?|file)://.*") ? new URL(specLocation) : new File(specLocation).toURL()

    def bis = new BufferedInputStream(specUrl.openStream())
    byte[] buffer = new byte[1024]
    int bytesRead = 0
    def baos = new ByteArrayOutputStream()
    while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
        baos.write(buffer, 0, bytesRead)
    }
    bis.close()
    return baos.toByteArray()
}

def getSpecVersion(def specLocation, def specBytes) {
    ObjectMapper mapper = new ObjectMapper(specLocation.endsWith(".json") ? new JsonFactory() : new YAMLFactory())
    mapper.findAndRegisterModules()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    return mapper.readValue(specBytes, Map.class).get("openapi")
}

def ensureYAML(def specLocation, def bytes) {
    return specLocation.endsWith(".json") ? new YAMLMapper().writeValueAsString(new ObjectMapper().readTree(new String(bytes))) : new String(bytes)
}

String spec = request.getProperties().get("spec")
def specBytes = readSpec(spec)
def version = getSpecVersion(spec, specBytes)
def specDocStr = ensureYAML(spec, specBytes)

File outputDir = new File(request.getOutputDirectory() + File.separator + artifactId)

File proxyFile = new File(outputDir, "src/main/gateway/template-v1/apiproxy/proxies/default.xml")
File targetFile = new File(outputDir, "src/main/gateway/template-v1/apiproxy/targets/default.xml")

Document proxyDoc = readDocument(proxyFile)
Document targetDoc = readDocument(targetFile)

if (version.matches("3\\..*")) {
    OpenAPI openAPI = new OpenAPIV3Parser().readContents(specDocStr).openAPI
    for (def pathSuffix: openAPI.paths.keySet()) {
        for (def httpMethod: openAPI.paths.get(pathSuffix).readOperationsMap().keySet()) {
            Operation operation = openAPI.paths.get(pathSuffix).readOperationsMap().get(httpMethod)
            addFlowCondition(proxyDoc, pathSuffix, httpMethod.toString(), operation.getOperationId(), operation.getSummary())
            addFlowCondition(targetDoc, pathSuffix, httpMethod.toString(), operation.getOperationId(), operation.getSummary())
        }
    }
}

writeDocument(proxyDoc, proxyFile)
writeDocument(targetDoc, targetFile)


/*

// Move contents from root dir to project.basedir
def rootDir = new File(outputDir, "root")
for (File rootFile: rootDir.listFiles()) {
    if (rootFile.isFile()) {
        File newRootFile = new File(outputDir, rootFile.getName())
        Files.move(rootFile.toPath(), newRootFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
rootDir.delete()



// Rename apiproxy directory from template-v1 to ${apiproxyName}
def apiproxyName = request.getProperties().get("apiproxyName") as String
def apiproxyDir = new File(outputDir, "src/main/gateway/template-v1")
if (!apiproxyName.equals("template-v1")) {
    apiproxyDir.renameTo(new File(outputDir, "src/main/gateway/" + apiproxyName))
    apiproxyDir = new File(outputDir, "src/main/gateway/" + apiproxyName)

    // Rename apiproxy descriptor
    def apiproxyDescriptor = new File(new File(apiproxyDir, "apiproxy"), "template-v1.xml")
    apiproxyDescriptor.renameTo(new File(new File(apiproxyDir, "apiproxy"), apiproxyName + ".xml"))

    // Rename edge/api/template-v1 to edge/api/${apiproxyName}
    def edgeApiproxyDir = new File(apiproxyDir, "edge/api/template-v1")
    edgeApiproxyDir.renameTo(new File(apiproxyDir, "edge/api/" + apiproxyName))

    // Rename apiproxy directory from template-v1 to ${apiproxyName}
    def testApiproxyDir = new File(outputDir, "src/test/gateway/unit/template-v1")
    testApiproxyDir.renameTo(new File(outputDir, "src/test/gateway/unit/" + apiproxyName))

    // Rename apiproxy directory from template-v1 to ${apiproxyName}
    def perfomanceApiproxyDir = new File(outputDir, "src/test/gateway/performance/template-v1")
    perfomanceApiproxyDir.renameTo(new File(outputDir, "src/test/gateway/performance/" + apiproxyName))
}

// Create main/java directory estructure to match the package name
def mainJavaDir = new File(apiproxyDir, "callout/src/main/java")
def packageName = request.getProperties().get("javaCalloutPackage").replace("-", "_")
def groupId = request.getProperties().get("groupId")
def packageDir = (groupId + packageName.substring(10)).replace(".", "/") as String
def mainTargetDir = new File(mainJavaDir, packageDir)
mainTargetDir.mkdirs()
for (File javaFile: mainJavaDir.listFiles()) {
    if (javaFile.isFile()) {
        File targetFile = new File(mainTargetDir, javaFile.getName());
        Files.move(javaFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

// Create test/java directory estructure to match the package name
def testJavaDir = new File(apiproxyDir, "callout/src/test/java")
def testTargetDir = new File(testJavaDir, packageDir)
testTargetDir.mkdirs()
for (File javaFile: testJavaDir.listFiles()) {
    if (javaFile.isFile()) {
        File targetFile = new File(testTargetDir, javaFile.getName());
        Files.move(javaFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
new File(mainJavaDir.getParent(), "resources").mkdirs()
new File(testJavaDir.getParent(), "resources").mkdirs()

def pw = new PrintWriter(new File(outputDir, "autogenerate-project.properties"))
request.getProperties().list(pw)
pw.close()
*/