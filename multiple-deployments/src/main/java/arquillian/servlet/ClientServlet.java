package arquillian.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;

@WebServlet(ClientServlet.URL_PATTERN)
public class ClientServlet extends HttpServlet {

    public static final String URL_PATTERN = "/client";

    public static final String MESSAGE_PARAM = "message";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/hello/hello");
        Response resp = target.request().get();
        String content = resp.readEntity(String.class);
        response.getWriter().append(content).append(" + ").append(request.getParameter(MESSAGE_PARAM));
    }
}
