import java.nio.file.Files
import java.nio.file.StandardCopyOption

File outputDir = new File(request.getOutputDirectory() + File.separator + artifactId)

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

    // Rename apiproxy directory from template-v1 to ${apiproxyName}
    def testApiproxyDir = new File(outputDir, "src/test/gateway/unit/template-v1")
    testApiproxyDir.renameTo(new File(outputDir, "src/test/gateway/unit/" + apiproxyName))

    // Rename apiproxy directory from template-v1 to ${apiproxyName}
    def perfomanceApiproxyDir = new File(outputDir, "src/test/gateway/performance/template-v1")
    perfomanceApiproxyDir.renameTo(new File(outputDir, "src/test/gateway/performance/" + apiproxyName))
}

// Create main/java directory estructure to match the package name
def mainJavaDir = new File(apiproxyDir, "callout/src/main/java")
def packageDir = request.getProperties().get("javaCalloutPackage").replace(".", File.separator) as String
def mainTargetDir = new File(mainJavaDir, packageDir)
mainTargetDir.mkdirs()
for (File javaFile: mainJavaDir.listFiles()) {
    if (javaFile.isFile()) {
        Files.move(javaFile.toPath(), (new File(mainTargetDir, javaFile.getName())).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

// Create test/java directory estructure to match the package name
def testJavaDir = new File(apiproxyDir, "callout/src/test/java")
def testTargetDir = new File(testJavaDir, packageDir)
testTargetDir.mkdirs()
for (File javaFile: testJavaDir.listFiles()) {
    if (javaFile.isFile()) {
        Files.move(javaFile.toPath(), (new File(testTargetDir, javaFile.getName())).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
new File(mainJavaDir.getParent(), "resources").mkdirs()
new File(testJavaDir.getParent(), "resources").mkdirs()