package components.dbox.hasher;

import org.junit.Test;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class DBoxHashHelperTest {
    @Test
    public void testGetHash() throws NoSuchAlgorithmException {
        DBoxHashHelper dboxHashHelper = new DBoxHashHelper(new DBoxHasher());
        assertEquals(dboxHashHelper.getHash(new File("src/test/resources/scratches/1.md")), "5df7bc577d21206f56f7ee860bf1f67600f3985813ce2eca2ca456259e779d0b");
        assertEquals(dboxHashHelper.getHash(new File("src/test/resources/scratches/2.md")), "2d9ea99bd061f3c446e2b85533b595fb8390ba1caacaf772b50088ec346323f2");
        assertEquals(dboxHashHelper.getHash(new File("src/test/resources/scratches/3.md")), "7a02ab1cdbc07237616de0469bfdd4f84db5af0bf0183492dfb1c891e749ca26");
    }

    @Test
    public void testHex() throws NoSuchAlgorithmException {
        DBoxHashHelper dboxHashHelper = new DBoxHashHelper(new DBoxHasher());
        String hex = dboxHashHelper.hex(new byte[]{-29, -80, -60, 66 - 104, -4, 28, 20 - 102, -5, -12, -56, -103, 111 - 71, 36, 39, -82, 65 - 28, 100, -101, -109, 76 - 92, -107, -103, 27, 120, 82, -72, 85});
        assertEquals("e3b0c4dafc1caefbf4c899282427ae25649b93f095991b7852b855", hex);
        String hex1 = dboxHashHelper.hex(new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        assertEquals("414141414141414141414141414141414141414141414141", hex1);
    }

}

