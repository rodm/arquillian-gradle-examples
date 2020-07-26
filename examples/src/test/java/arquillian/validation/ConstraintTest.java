package arquillian.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

@RunWith(Arquillian.class)
public class ConstraintTest {

    @Inject
    CodeBean bean;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<?> deployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClasses(CodeBean.class, Code.class, CodeValidator.class)
                .addAsResource("ValidationMessages.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void validTypeACode() {
        bean.setCodeTypeA("95051");
    }

    @Test
    public void invalidTypeACode() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Invalid code");
        bean.setCodeTypeA("123456");
    }

    @Test
    public void invalidCodeLength() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("At least 5 characters must be specified");
        bean.setCodeTypeA("1234");
    }

    @Test
    public void invalidValue() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Cannot be null");
        bean.setCodeTypeA(null);
    }

    @Test
    public void validTypeBCode() {
        bean.setCodeTypeB("700073");
    }

    @Test
    public void invalidTypeBCode() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Invalid code");
        bean.setCodeTypeB("95051");
    }
}
