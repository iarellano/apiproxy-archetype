import io.apigee.buildTools.enterprise4g.utils.ServerProfile

class Profile {
    def getProperty(project, name, defaultValue) {
        def value = project.getProperties().get(name);
        if ("".equals(value) || value == null) {
            return defaultValue;
        }
        return value;
    }

    def getProfile(project) {
        def serverProfile = new ServerProfile();
        serverProfile.setOrg(project.getProperties().get("apigee.org"));
        serverProfile.setApplication(project.getName());
        serverProfile.setApi_version(project.getProperties().get("apigee.apiversion"));
        serverProfile.setApi_type(project.getProperties().get("apigee.apitype"));
        serverProfile.setHostUrl(project.getProperties().get("apigee.hosturl"));
        serverProfile.setTokenUrl(getProperty(project, "apigee.tokenurl", "https://login.apigee.com/oauth/token"));
        serverProfile.setMFAToken(project.getProperties().get("pigee.mfatoken"));
        serverProfile.setAuthType(getProperty(project, "apigee.authtype", "basic"));
        serverProfile.setEnvironment(getProperty(project, "apigee.env", project.getProperties().get("apigee.profile")));
        serverProfile.setBearerToken(project.getProperties().get("apigee.bearer"));
        serverProfile.setRefreshToken(project.getProperties().get("apigee.refresh"));
        serverProfile.setCredential_user(project.getProperties().get("apigee.username"));
        serverProfile.setCredential_pwd(project.getProperties().get("apigee.password"));
        serverProfile.setClientId(project.getProperties().get("apigee.clientid"));
        serverProfile.setClientSecret(project.getProperties().get("apigee.clientsecret"));
        serverProfile.setProfileId(project.getProperties().get("apigee.profile"));
        serverProfile.setOptions(project.getProperties().get("apigee.options"));
        serverProfile.setDelay(Long.valueOf(getProperty(project, "apigee.delay", "0")));
        serverProfile.setOverridedelay(Long.valueOf(getProperty(project, "apigee.override.delay", "0")));
        serverProfile.setRevision(Long.valueOf(getProperty(project, "apigee.revision", "0")));
        return serverProfile;
    }
}