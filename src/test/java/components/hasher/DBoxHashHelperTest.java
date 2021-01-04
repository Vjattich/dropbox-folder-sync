package components.hasher;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.Ignore;
import org.junit.Test;

public class DBoxHashHelperTest {
    @Test
    @Ignore
    public void testGetHash() throws NoSuchAlgorithmException {
        DBoxHashHelper dboxHashHelper = new DBoxHashHelper(new DBoxHasher());
        dboxHashHelper.getHash(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    }

    @Test
    @Ignore
    public void testHex() throws NoSuchAlgorithmException {
        DBoxHashHelper dboxHashHelper = new DBoxHashHelper(new DBoxHasher());
        String hex = dboxHashHelper.hex(new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        assertEquals("2a846fa617c3361fc117e1c5c1e1838c336b6a5cef982c1a2d9bdf68f2f1992a", hex);
        String hex1 = dboxHashHelper.hex(new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        assertEquals("c68469027410ea393eba6551b9fa1e26db775f00eae70a0c3c129a0011a39cf9", hex1);
        String hex2 = dboxHashHelper.hex(new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        assertEquals("7376192de020925ce6c5ef5a8a0405e931b0a9a8c75517aacd9ca24a8a56818b", hex2);
    }

}

