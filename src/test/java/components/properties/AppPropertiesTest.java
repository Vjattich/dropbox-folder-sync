package components.properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AppPropertiesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorTest() {
        thrown.expect(RuntimeException.class);
        new AppProperties(new PropertiesComponent("123123"));
    }

    @Test
    public void constructorTest2() {
        thrown.expect(RuntimeException.class);
        new AppProperties(new PropertiesComponent(""));
    }

    @Test
    public void methodTest() {
        AppProperties appProperties = new AppProperties(new PropertiesComponent("src/test/resources/fake.properties"));
        assertThat("client identifier are equal", appProperties.getClientIdentifier(), equalTo("identifier"));
        assertThat("dropbox auth are equal", appProperties.getDBoxAuth(), equalTo("oauth2"));
        assertThat("sync folder are equal", appProperties.getSyncFolder(), equalTo("sync_folder"));
        assertThat("sync time are equal", appProperties.getSyncTime(), equalTo(120000L));
    }

}