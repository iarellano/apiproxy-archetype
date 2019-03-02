import io.apigee.buildTools.enterprise4g.utils.ServerProfile
import com.google.api.client.http.HttpRequest

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class RestUtil {

    private static def executeApiMethod;

    private static def restUtil

    static def REQUEST_FACTORY

    static {
        Field REQUEST_FACTORY_FIELD = io.apigee.buildTools.enterprise4g.rest.RestUtil.class.getDeclaredField("REQUEST_FACTORY")
        REQUEST_FACTORY_FIELD.setAccessible(true)
        this.REQUEST_FACTORY = REQUEST_FACTORY_FIELD.get(null)

        executeApiMethod = io.apigee.buildTools.enterprise4g.rest.RestUtil.class.getDeclaredMethod("executeAPI", ServerProfile.class, HttpRequest.class)
        executeApiMethod.setAccessible(true);

        restUtil = new io.apigee.buildTools.enterprise4g.rest.RestUtil();
    }

    def executeApi(profile, request) {
        try {
            return executeApiMethod.invoke(restUtil, profile, request)
        } catch (java.lang.reflect.InvocationTargetException ite) {
            throw ite.getCause() == null ? ite : ite.getCause()
        }
    }

    def getLatestRevision(profile) {
        return restUtil.getLatestRevision(profile);
    }

    def getDeployedRevision(profile) {
        return restUtil.getDeployedRevision(profile)
    }
}