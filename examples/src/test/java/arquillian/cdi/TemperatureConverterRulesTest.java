package arquillian.cdi;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.ArquillianTest;
import org.jboss.arquillian.junit.ArquillianTestClass;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class TemperatureConverterRulesTest {

    @ClassRule
    public static ArquillianTestClass arquillianTestClass = new ArquillianTestClass();

    @Rule
    public ArquillianTest arquillianTest = new ArquillianTest();

    @Inject
    private TemperatureConverter converter;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(TemperatureConverter.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    @Test
    public void testConvertToCelsius() {
        assertEquals(converter.convertToCelsius(32d), 0d, 0.01);
        assertEquals(converter.convertToCelsius(212d), 100d, 0.01);
    }

    @Test
    public void testConvertToFarenheit() {
        assertEquals(converter.convertToFarenheit(0d), 32d, 0.01);
        assertEquals(converter.convertToFarenheit(100d), 212d, 0.01);
    }
}
