package arquillian.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/book")
public class BookResource {

    private static final Book[] BOOKS = {
            new Book(1, "1984", "George Orwell"),
            new Book(5, "Strange in a Strange Land", "Robert A Heinlein"),
            new Book(7, "The Man in the High Castle", "Philip K Dick")
    };

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Book> getBooks() {
        return Arrays.asList(BOOKS);
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Book getBook(@PathParam("id") long id) {
        return findBookById(id);
    }

    private Book findBookById(long id) {
        for (Book book : BOOKS) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
}
