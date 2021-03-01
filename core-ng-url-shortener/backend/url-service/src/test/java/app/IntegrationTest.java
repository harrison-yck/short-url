package app;

import core.framework.test.Context;
import core.framework.test.IntegrationExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationExtension.class)
@Context(module = TestModule.class)
public class IntegrationTest {
    protected IntegrationTest() {
    }
}
