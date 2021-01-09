package components.dbox.hasher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class DBoxHasherTest {

    @Rule
    public ExpectedException testEngineUpdate8ExceptionRule = ExpectedException.none();
    @Rule
    public ExpectedException testEngineDigestExceptionRule = ExpectedException.none();

    @Test
    public void testConstructor() throws NoSuchAlgorithmException {
        assertEquals(32, (new DBoxHasher()).getDigestLength());
        assertEquals(32, (new DBoxHasher()).getDigestLength());
    }

    @Test
    public void testEngineUpdate() throws NoSuchAlgorithmException {
        (new DBoxHasher()).engineUpdate((byte) 65);
    }

    @Test
    public void testEngineUpdate2() throws NoSuchAlgorithmException {
        ByteBuffer input = ByteBuffer.allocate(3);
        (new DBoxHasher()).engineUpdate(input);
    }

    @Test
    public void testEngineUpdate6() throws NoSuchAlgorithmException {
        ByteBuffer input = ByteBuffer.allocate(3);
        (new DBoxHasher()).engineUpdate(input);
    }

    @Test
    public void testEngineUpdate7() throws NoSuchAlgorithmException {
        (new DBoxHasher()).engineUpdate(
                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}, 2, 3
        );
    }

    @Test
    public void testEngineUpdate8() throws NoSuchAlgorithmException {
        testEngineUpdate8ExceptionRule.expect(java.lang.IllegalArgumentException.class);
        (new DBoxHasher()).engineUpdate(new byte[]{}, 2, 3);
    }

    @Test
    public void testEngineGetDigestLength() throws NoSuchAlgorithmException {
        assertEquals(32, (new DBoxHasher()).engineGetDigestLength());
        assertEquals(32, (new DBoxHasher()).engineGetDigestLength());
    }

    @Test
    public void testEngineDigest() throws NoSuchAlgorithmException {
        byte[] actualEngineDigestResult = (new DBoxHasher()).engineDigest();
        assertEquals(32, actualEngineDigestResult.length);
        assertEquals((byte) -29, actualEngineDigestResult[0]);
        assertEquals((byte) -80, actualEngineDigestResult[1]);
        assertEquals((byte) -60, actualEngineDigestResult[2]);
        assertEquals((byte) 66, actualEngineDigestResult[3]);
        assertEquals((byte) -104, actualEngineDigestResult[4]);
        assertEquals((byte) -4, actualEngineDigestResult[5]);
        assertEquals((byte) -103, actualEngineDigestResult[26]);
        assertEquals((byte) 27, actualEngineDigestResult[27]);
        assertEquals((byte) 120, actualEngineDigestResult[28]);
        assertEquals((byte) 82, actualEngineDigestResult[29]);
        assertEquals((byte) -72, actualEngineDigestResult[30]);
        assertEquals((byte) 85, actualEngineDigestResult[31]);
    }

    @Test
    public void testEngineDigest2() throws NoSuchAlgorithmException {
        DBoxHasher dBoxHasher = new DBoxHasher();
        dBoxHasher.update(new byte[]{65, 65, 65, 65, 65, 65, 65, 65});
        byte[] actualEngineDigestResult = dBoxHasher.engineDigest();
        assertEquals(32, actualEngineDigestResult.length);
        assertEquals((byte) 100, actualEngineDigestResult[0]);
        assertEquals((byte) -102, actualEngineDigestResult[1]);
        assertEquals((byte) -106, actualEngineDigestResult[2]);
        assertEquals((byte) 108, actualEngineDigestResult[3]);
        assertEquals((byte) -66, actualEngineDigestResult[4]);
        assertEquals((byte) 63, actualEngineDigestResult[5]);
        assertEquals((byte) -31, actualEngineDigestResult[26]);
        assertEquals((byte) -67, actualEngineDigestResult[27]);
        assertEquals((byte) -79, actualEngineDigestResult[28]);
        assertEquals((byte) 65, actualEngineDigestResult[29]);
        assertEquals((byte) 90, actualEngineDigestResult[30]);
        assertEquals((byte) -16, actualEngineDigestResult[31]);
    }

    @Test
    public void testEngineDigest4() throws DigestException, NoSuchAlgorithmException {
        testEngineDigestExceptionRule.expect(DigestException.class);
        DBoxHasher dBoxHasher = new DBoxHasher();
        dBoxHasher.update(new byte[]{65, 65, 65, 65, 65, 65, 65, 65});
        dBoxHasher.engineDigest(
                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}, 2, 3
        );
    }

    @Test
    public void testEngineDigest7() throws DigestException, NoSuchAlgorithmException {
        testEngineDigestExceptionRule.expect(DigestException.class);
        (new DBoxHasher()).engineDigest(
                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}, 2, 3
        );
    }


    @Test
    public void testEngineReset() throws NoSuchAlgorithmException {
        (new DBoxHasher()).engineReset();
    }

    @Test
    public void testClone() throws CloneNotSupportedException, NoSuchAlgorithmException {
        assertEquals(32, (new DBoxHasher()).clone().getDigestLength());
        assertEquals(32, (new DBoxHasher()).clone().getDigestLength());
    }

}

