package arquillian.jpa;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Arquillian.class)
public class BookTest {

    @Deployment
    public static Archive<?> createDeployment() {
        String persistenceResourceName = getPersistenceResourceName();
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Book.class.getPackage())
                .addAsResource(persistenceResourceName, "META-INF/persistence.xml")
                .addAsWebInfResource("glassfish-resources.xml")
                .addAsWebInfResource("wildfly-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(war.toString(true));
        return war;
    }

    private static String getPersistenceResourceName() {
        String launchName = System.getProperty("arquillian.launch", "glassfish41");
        if (launchName.contains("glassfish")) {
            return "glassfish-persistence.xml";
        } else if (launchName.contains("wildfly")) {
            return "wildfly-persistence.xml";
        }
        return "";
    }

    private static final Book[] BOOKS = {
            new Book("1984", "George Orwell"),
            new Book("Strange in a Strange Land", "Robert A Heinlein"),
            new Book("The Man in the High Castle", "Philip K Dick")
    };

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Deleting old records...");
        em.createQuery("DELETE FROM Book").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (Book book : BOOKS) {
            em.persist(book);
        }
        utx.commit();
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    @Test
    public void shouldFindAllBooksUsingJpqlQuery() {
        // given
        String fetchingAllBooksInJpql = "select b from Book b order by b.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Book> books = em.createQuery(fetchingAllBooksInJpql, Book.class).getResultList();

        // then
        System.out.println("Found " + books.size() + " books (using JPQL):");
        for (Book book : books) {
            System.out.println("* " + book);
        }

        List<String> actualTitles = books.stream().map(Book::getTitle).collect(toList());
        List<String> expectedTitles = Arrays.stream(BOOKS).map(Book::getTitle).collect(toList());
        assertThat(actualTitles, equalTo(expectedTitles));
    }
}
