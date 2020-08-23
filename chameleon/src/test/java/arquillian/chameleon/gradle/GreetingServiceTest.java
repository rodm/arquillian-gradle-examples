package arquillian.chameleon.gradle;

import arquillian.chameleon.GreeterService;
import org.arquillian.container.chameleon.api.ChameleonTarget;
import org.arquillian.container.chameleon.deployment.gradle.GradleBuild;
import org.arquillian.container.chameleon.runner.ArquillianChameleon;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@Ignore
@RunWith(ArquillianChameleon.class)
@GradleBuild(path = "../chameleon")
@ChameleonTarget("wildfly:15.0.1.Final:managed")
public class GreetingServiceTest {

    @Inject
    private GreeterService service;

    @Test
    public void shouldGreetTheWorld() throws Exception {
        assertEquals("hello world", service.hello());
    }
}
