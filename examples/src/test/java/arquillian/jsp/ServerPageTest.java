package arquillian.jsp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static arquillian.util.StreamReader.readAllAndClose;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Arquillian.class)
public class ServerPageTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(Greeter.class)
                .addAsWebResource("index.jsp", "index.jsp")
        ;
        System.out.println(war.toString(true));
        return war;
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void greeting() throws Exception {
        String requestUrl = deploymentUrl.toString();
        String body = readAllAndClose(new URL(requestUrl).openStream());

        assertThat(body, containsString("Greeting: Hello, world"));
    }
}
