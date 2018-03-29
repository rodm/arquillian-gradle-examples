package arquillian.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(EchoServlet.URL_PATTERN)
public class EchoServlet extends HttpServlet {

    public static final String URL_PATTERN = "/echo";

    public static final String MESSAGE_PARAM = "message";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().append(request.getParameter(MESSAGE_PARAM));
    }
}
