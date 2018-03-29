package arquillian.servlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static arquillian.util.StreamReader.readAllAndClose;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EchoServletTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "servlet-test.war")
                .addClass(EchoServlet.class);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void shouldBeAbleToInvokeServletInDeployedWebApp() throws Exception {
        String requestUrl = deploymentUrl + EchoServlet.URL_PATTERN.substring(1) + "?" + EchoServlet.MESSAGE_PARAM + "=hello";
        String body = readAllAndClose(new URL(requestUrl).openStream());

        assertEquals("Verify that the servlet was deployed and returns expected result", "hello", body);
    }
}
