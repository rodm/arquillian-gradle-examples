package arquillian.payara;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BookDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Book> getAll() {
        return entityManager.createNamedQuery("Book.getAll", Book.class).getResultList();
    }
}
