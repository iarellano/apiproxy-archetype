import Profile
import RestUtil

def profile = new Profile();
def serverProfile = profile.getProfile(project)

def restUtil = new RestUtil();
def latestRevision = restUtil.getLatestRevision(serverProfile)
def targetRevision = 1
if (!"".equals(latestRevision)) {
    targetRevision = Integer.parseInt(latestRevision) + 1
}

def deploymentSuffix = project.getProperties().get("deployment.suffix")
def resourceSuffix = deploymentSuffix + "-rev" + targetRevision
def sharedResourceName = project.getProperties().get("apiproxy.name") + resourceSuffix

project.getProperties().setProperty("resource.suffix", resourceSuffix)
project.getProperties().setProperty("target.revision", String.valueOf(targetRevision))
project.getProperties().setProperty("shared.resource.name", sharedResourceName)

log.info "Target revision: " + targetRevision
log.info "Resource suffix: " + resourceSuffix
log.info "Shared resource name: " + sharedResourceName