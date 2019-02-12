package ${javaCalloutPackage};

import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public abstract class AbstractExecution implements Execution {

    protected Map<String, Object> properties;

    protected String stackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return  sw.toString();
    }

    protected Object resolvePropertyValue(String propertyValue, MessageContext mc) {
        try {
            if (propertyValue.startsWith("{") && propertyValue.endsWith("}")) {
                return mc.getVariable(propertyValue.substring(1, propertyValue.length() - 1));
            }
            return propertyValue;
        } catch (Exception e) {
            mc.setVariable("flow.key-error", e.getMessage());
            mc.setVariable("flow.key-stack-trace", stackTraceToString(e));
            throw e;
        }
    }

    public AbstractExecution(Map<String, Object> properties) {
        this.properties = properties;
    }
}
