package ${javaCalloutPackage};

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.message.MessageContext;

import java.util.Map;

class ExecutionCallout extends AbstractExecution {

    public ExecutionCallout(Map<String, Object> properties) {
        super(properties);
    }

    @Override
    public ExecutionResult execute(MessageContext mc, ExecutionContext executionContext) {
        try {

            // Do some logic

            return ExecutionResult.SUCCESS;
        } catch (Exception e) {
            String message = stackTraceToString(e);
            mc.setVariable("ExecutionCallout.ERROR", message);
            return ExecutionResult.ABORT;
        }
    }
}