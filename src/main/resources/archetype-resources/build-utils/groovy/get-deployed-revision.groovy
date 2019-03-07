import Profile
import RestUtil

for (mavenProfile in project.getActiveProfiles()) {
    if ("deploy".equals(mavenProfile.getId())) {
        log.info("Skipping since deploy profile is active.")
        return
    }
}

def deployProfileActivated = project.getProperties().get("deployProfileActivated")
if ("true".equals(deployProfileActivated)) {
    log.info("Skipping getting deployed revision since deploy profile is active.")
    return
}

def profile = new Profile();
def serverProfile = profile.getProfile(project)

def restUtil = new RestUtil();
def deployedRevision = restUtil.getDeployedRevision(serverProfile)

if ("".equals(deployedRevision) && "true".equals(project.getProperties().get("fail.missing.deployed.revision"))) {
    throw new IllegalStateException("There is no deployed revision of API Proxy '" + serverProfile.getApplication() + "' on environment '" + serverProfile.getEnvironment() + "' to run integration testing.")
}

def deploymentSuffix = project.getProperties().get("deployment.suffix")
def resourceSuffix = deploymentSuffix + "-rev" + deployedRevision
def sharedResourceName = project.getProperties().get("apiproxy.name") + resourceSuffix

project.getProperties().setProperty("resource.suffix", resourceSuffix)
project.getProperties().setProperty("target.revision", deployedRevision)
project.getProperties().setProperty("shared.resource.name", sharedResourceName)
project.getProperties().setProperty("deployed.revision", deployedRevision)

log.info "Target revision: " + deployedRevision
log.info "Resource suffix: " + resourceSuffix
log.info "Shared resource name: " + sharedResourceName

