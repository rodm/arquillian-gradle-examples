package arquillian.websocket;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class WebSocketServerTest {

    @ArquillianResource
    URI base;

    @Deployment()
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(WebSocketServer.class, WebSocketClient1.class, WebSocketClient2.class);
    }

    @Test
    public void testConnect() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        WebSocketClient1.latch = new CountDownLatch(1);
        final Session session1 = connectToServer(WebSocketClient1.class);
        assertNotNull(session1);
        assertTrue(WebSocketClient1.latch.await(2, TimeUnit.SECONDS));
        assertEquals(WebSocketClient1.TEXT, WebSocketClient1.response);

        WebSocketClient1.latch = new CountDownLatch(1);
        WebSocketClient2.latch = new CountDownLatch(1);
        final Session session2 = connectToServer(WebSocketClient2.class);
        assertNotNull(session2);
        assertTrue(WebSocketClient1.latch.await(2, TimeUnit.SECONDS));
        assertTrue(WebSocketClient2.latch.await(2, TimeUnit.SECONDS));
        assertEquals(WebSocketClient2.TEXT, WebSocketClient1.response);
        assertEquals(WebSocketClient2.TEXT, WebSocketClient2.response);
    }

    public Session connectToServer(Class<?> endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://"
                + base.getHost()
                + ":"
                + base.getPort()
                + base.getPath()
                + "chat");
        return container.connectToServer(endpoint, uri);
    }
}
