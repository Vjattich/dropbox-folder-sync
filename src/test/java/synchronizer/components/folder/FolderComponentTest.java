package synchronizer.components.folder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.HashMap;

public class FolderComponentTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() {
        thrown.expect(IllegalArgumentException.class);
        new FolderComponent("Folder Path", new HashMap<>());
    }

    @Test
    public void testConstructor2() {
        thrown.expect(IllegalArgumentException.class);
        new FolderComponent(new File("src/test/resources/scratches/1.md").getPath(), new HashMap<>());
    }

    @Test
    public void testConstructor3() {
        new FolderComponent(new File("src/test/resources/scratches/").getPath(), new HashMap<>());
    }

}

