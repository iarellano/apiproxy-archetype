import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

def artifactId = request.getArtifactId()/*.replace("-", "_")*/ as String
def packageName = request.getPackage() as String
def packagePath = packageName.replace(".", File.separator) as String

class FileUtils {

    void moveFiles(File source, File target, File originalTarget) {
        if (source.equals(target)) return;
        for (File file : source.listFiles()) {
            if (file.isDirectory()) {
                println "Moving directory: " + file
                if (file.equals(originalTarget)) continue;
                File newTarget = new File(target, file.getName())
                newTarget.mkdirs()
                moveFiles(file, newTarget)
            } else {
                File newFile = new File(target, file.getName())
                Path path = newFile.toPath()
                println "Moving file: " + file
                Files.move(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

    void moveFiles(File source, File target) {
        moveFiles(source, target, target)
    }

    def removeRecursive(File directory, boolean includeThis) {
        if (!directory.isDirectory()) {
            if (includeThis) {
                // println "Deleteing directory: " + directory
                directory.delete();
            }
            return
        }
        for (File file: directory.listFiles()) {
            if (file.isDirectory()) {
                removeRecursive(file, true)
            } else {
                // println "Deleteing file: " + file
                file.delete()
            }
        }
        if (includeThis) {
            // println "Deleteing directory: " + directory
            directory.delete()
        }
    }
}

FileUtils fileUtils = new FileUtils();


File outputDir = new File(request.getOutputDirectory() + File.separator + artifactId)

fileUtils.moveFiles(new File(outputDir, "misc"), outputDir)
new File(outputDir, "misc").delete()


File srcMain = new File(outputDir, "src/main/callout/src/main/java")
File dest = new File(srcMain, packagePath)
dest.mkdirs()
for (File file: srcMain.listFiles()) {
    if (file.getName().endsWith("java")) {
        Files.move(file.toPath(), new File(dest, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

File srcTest = new File(outputDir, "src/main/callout/src/test/java")
dest = new File(srcTest, packagePath)
dest.mkdirs()
for (File file: srcTest.listFiles()) {
    if (file.getName().endsWith("java")) {
        Files.move(file.toPath(), new File(dest, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

//File calloutDir = new File(outputDir, "src/callout")
//
//FileUtils fileUtils = new FileUtils();
//
//File srcMain = new File(calloutDir, "src/main/java/" + packagePath)
//new File(calloutDir, "src/main/resources").mkdirs()
//File srcTest = new File(calloutDir, "src/test/java/" + packagePath)
//new File(calloutDir, "src/test/resources").mkdirs()
//
//srcMain.mkdirs()
//srcTest.mkdirs()
//
//fileUtils.moveFiles(new File(calloutDir, packagePath + "/src/main/java"), srcMain)
//fileUtils.moveFiles(new File(calloutDir, packagePath + "/src/test/java"), srcTest)
////Files.move(new File(calloutDir, packagePath + "/pom.xml").toPath(), new File(calloutDir, "pom.xml").toPath(), StandardCopyOption.REPLACE_EXISTING)
//fileUtils.removeRecursive(new File(calloutDir, "com"), true)
//
//println request.toString()
//println request.getOutputDirectory()
//println request.getGroupId()
//println request.getArtifactId()
//println request.getPackage()
//println request.getProperties().list(System.out)