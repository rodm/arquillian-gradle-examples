package arquillian.servlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static arquillian.servlet.StreamReaderUtil.readAllAndClose;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EchoServletTest {

    @TargetsContainer("container1")
    @Deployment(name = "deployment1", testable = false)
    public static WebArchive createDeployment1() {
        return ShrinkWrap.create(WebArchive.class, "servlet-test1.war")
                .addClass(EchoServlet.class);
    }

    @TargetsContainer("container2")
    @Deployment(name = "deployment2", testable = false)
    public static WebArchive createDeployment2() {
        return ShrinkWrap.create(WebArchive.class, "servlet-test2.war")
                .addClass(EchoServlet.class);
    }

    @Test @OperateOnDeployment("deployment1")
    public void invokeServletOnContainer1(@ArquillianResource URL deploymentUrl) throws Exception {
        String requestUrl = deploymentUrl + EchoServlet.URL_PATTERN.substring(1) + "?" + EchoServlet.MESSAGE_PARAM + "=container1";
        String body = readAllAndClose(new URL(requestUrl).openStream());

        assertEquals("Verify that the servlet was deployed and returns expected result", "container1", body);
    }

    @Test @OperateOnDeployment("deployment2")
    public void invokeServletOnContainer2(@ArquillianResource URL deploymentUrl) throws Exception {
        String requestUrl = deploymentUrl + EchoServlet.URL_PATTERN.substring(1) + "?" + EchoServlet.MESSAGE_PARAM + "=container2";
        String body = readAllAndClose(new URL(requestUrl).openStream());

        assertEquals("Verify that the servlet was deployed and returns expected result", "container2", body);
    }
}
