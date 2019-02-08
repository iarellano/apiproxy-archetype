package ${package};

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.message.MessageContext;
import mockit.MockUp;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.reflect.core.Reflection.method;

public class ExecutionCalloutTest {

    private ExecutionContext exeCtxt;
    private Map<String, Object> properties;

    @BeforeMethod
    public void setUp() throws Exception {
        exeCtxt = new MockUp<ExecutionContext>(){}.getMockInstance();
        properties = new HashMap<>();
        properties.put("test.data", "{request.content}");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        properties.clear();
    }

    @Test
    public void testExecuteInvalidData() {

        Map<String, Object> properties = new HashMap<>(this.properties);
        MessageContext msgCtxt = MessageContextFactory.newInstance();


        ExecutionCallout callout = new ExecutionCallout(properties);

        ExecutionResult result = method("execute")
                .withReturnType(ExecutionResult.class)
                .withParameterTypes(MessageContext.class, ExecutionContext.class)
                .in(callout)
                .invoke(msgCtxt, null);

        Assert.assertEquals(result, ExecutionResult.SUCCESS);
    }
}