package arquillian.servlet;

import arquillian.util.StreamReader;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class RegisteredServletTest {

    @Deployment(testable = false)
    public static WebArchive getTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "servlet-test.war")
                .addClass(HelloServlet.class)
                .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class).version("3.0")
                        .createServlet()
                        .servletName(HelloServlet.class.getSimpleName())
                        .servletClass(HelloServlet.class.getName()).up()
                        .createServletMapping()
                        .servletName(HelloServlet.class.getSimpleName())
                        .urlPattern("/test").up()
                        .exportAsString()));
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void shouldBeAbleToInvokeServletInDeployedWebApp() throws Exception {
        String requestUrl = deploymentUrl + "test";
        String body = StreamReader.readAllAndClose(new URL(requestUrl).openStream());

        assertEquals("Verify that the servlet was deployed and returns expected result", "Hello, world", body);
    }
}
