package arquillian.chameleon.file;

import arquillian.chameleon.GreeterService;
import org.arquillian.container.chameleon.api.ChameleonTarget;
import org.arquillian.container.chameleon.deployment.file.File;
import org.arquillian.container.chameleon.runner.ArquillianChameleon;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(ArquillianChameleon.class)
@File("build/libs/hello.war")
@ChameleonTarget("wildfly:15.0.1.Final:managed")
public class GreetingServiceTest {

    @Inject
    private GreeterService service;

    @Test
    public void shouldGreetTheWorld() throws Exception {
        assertEquals("hello world", service.hello());
    }
}
