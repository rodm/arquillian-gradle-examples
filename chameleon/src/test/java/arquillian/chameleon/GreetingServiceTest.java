package arquillian.chameleon;

import org.arquillian.container.chameleon.api.ChameleonTarget;
import org.arquillian.container.chameleon.runner.ArquillianChameleon;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(ArquillianChameleon.class)
@ChameleonTarget("wildfly:15.0.1.Final:managed")
public class GreetingServiceTest {

    @Deployment
    public static WebArchive deployService() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(GreeterService.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private GreeterService service;

    @Test
    public void shouldGreetTheWorld() throws Exception {
        assertEquals("hello world", service.hello());
    }
}
