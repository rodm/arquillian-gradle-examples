package arquillian.servlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static arquillian.servlet.StreamReaderUtil.readAllAndClose;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class MultipleDeploymentTest {

    @Deployment(name = "deployment1", testable = false, order = 1)
    public static WebArchive createDeployment1() {
        return ShrinkWrap.create(WebArchive.class, "hello.war")
                .addClass(HelloServlet.class);
    }

    @Deployment(name = "deployment2", testable = false, order = 2)
    public static WebArchive createDeployment2() {
        return ShrinkWrap.create(WebArchive.class, "client.war")
                .addClass(ClientServlet.class);
    }

    @Test
    @OperateOnDeployment("deployment1")
    @InSequence(1)
    public void callServletInDeployment1(@ArquillianResource URL context) throws Exception {
        String requestUrl = context + HelloServlet.URL_PATTERN.substring(1);
        String body = readAllAndClose(new URL(requestUrl).openStream());

        assertEquals("Hello, world", body);
    }

    @Test
    @OperateOnDeployment("deployment2")
    @InSequence(2)
    public void callServletInDeployment2(@ArquillianResource URL context) throws Exception {
        String requestUrl = context + ClientServlet.URL_PATTERN.substring(1) + "?" + ClientServlet.MESSAGE_PARAM + "=message";
        String body = readAllAndClose(new URL(requestUrl).openStream());

        assertEquals("Hello, world + message", body);
    }
}
