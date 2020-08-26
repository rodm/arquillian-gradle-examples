package arquillian.payara;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class BookTest {

    @EJB
    private BookDAO bookDAO;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "arquillian-example.war")
                .addClass(Book.class)
                .addClass(BookDAO.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        System.out.println(archive.toString(true));
        return archive;
    }

    @Test
    @UsingDataSet("datasets/books.yml")
    public void shouldReturnAllBooks() throws Exception {
        List<Book> books = bookDAO.getAll();

        assertNotNull(books);
        assertThat(books.size(), is(3));

        List<String> titles = books.stream().map(Book::getTitle).collect(Collectors.toList());
        assertThat(titles, hasItem("The Man in the High Castle"));

        List<String> authors = books.stream().map(Book::getAuthor).collect(Collectors.toList());
        assertThat(authors, hasItem("Philip K Dick"));
    }
}
