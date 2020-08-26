package arquillian.jaxrs;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class BookTest {

    private static final String RESOURCE_PREFIX = BookApplication.class.getAnnotation(ApplicationPath.class).value().substring(1);

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Book.class, BookApplication.class, BookResource.class);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void getBookById() {
        // GET http://localhost:8080/test/rest/book/1
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(deploymentUrl.toString() + RESOURCE_PREFIX + "/book/1");
        Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);

        Response response = builder.get();

        assertEquals(200, response.getStatus());

        Book book = response.readEntity(Book.class);
        assertEquals(1L, book.getId());
        assertEquals("1984", book.getTitle());
        assertEquals("George Orwell", book.getAuthor());
    }

    @Test
    public void getBookByIdAsXml() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(deploymentUrl.toString() + RESOURCE_PREFIX + "/book/1");
        Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);

        String response = builder.get(String.class);

        String responseString = response.replaceAll("<\\?xml.*\\?>", "").trim();
        assertEquals("<book><author>George Orwell</author><id>1</id><title>1984</title></book>", responseString);
    }

    @Test
    public void getBookByIdAsJson() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(deploymentUrl.toString() + RESOURCE_PREFIX + "/book/1");
        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);

        String response = builder.get(String.class);

        JsonObject object = Json.createReader(new StringReader(response)).readObject();
        assertEquals(1, object.getInt("id"));
        assertEquals("1984", object.getString("title"));
        assertEquals("George Orwell", object.getString("author"));
    }
}
