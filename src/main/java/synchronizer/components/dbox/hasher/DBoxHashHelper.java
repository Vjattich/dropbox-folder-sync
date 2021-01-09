package synchronizer.components.dbox.hasher;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DBoxHashHelper {

    private final DBoxHasher hasher;

    private final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public DBoxHashHelper(DBoxHasher dBoxHasher) {
        this.hasher = dBoxHasher;
    }

    @SneakyThrows
    public String getHash(File file) {

        byte[] buf = new byte[1024];

        try (InputStream in = new FileInputStream(file)) {
            while (true) {
                int n = in.read(buf);
                if (n < 0) {
                    break;// EOF
                }
                hasher.update(buf, 0, n);
            }
        }

        return hex(hasher.digest());
    }

    public String hex(byte[] data) {
        char[] buf = new char[2 * data.length];
        int i = 0;
        for (byte b : data) {
            buf[i++] = HEX_DIGITS[(b & 0xf0) >>> 4];
            buf[i++] = HEX_DIGITS[b & 0x0f];
        }
        return new String(buf);
    }

}
