package components.dbox;

import com.dropbox.core.v2.files.Metadata;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public interface DBoxApi {

    @SneakyThrows
    void upload(File file);

    @SneakyThrows
    ByteArrayOutputStream download(String name);

    @SneakyThrows
    List<Metadata> getFolderEntries();

}
