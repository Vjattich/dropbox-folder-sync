package components.dbox.hasher;

import org.junit.Test;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class DBoxHashHelperTest {
    @Test
    public void testGetHash() throws NoSuchAlgorithmException {
        DBoxHashHelper dboxHashHelper = new DBoxHashHelper(new DBoxHasher());
        assertEquals(dboxHashHelper.getHash(new File("src/test/resources/scratches/1.md")), "4c7e488f75c03604cc22f891e36b32f42787fd9345a8df194be53248c6dfd476");
        assertEquals(dboxHashHelper.getHash(new File("src/test/resources/scratches/2.md")), "2b3a8ce336e7f1f8a9827b990c5509f9d9087453367335a259b5a3f4a36fa13f");
        assertEquals(dboxHashHelper.getHash(new File("src/test/resources/scratches/3.md")), "cdecb1f8600ed4e0069b61dc7ac17bd812f51874c930400ceb82f6eb25285b19");
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

