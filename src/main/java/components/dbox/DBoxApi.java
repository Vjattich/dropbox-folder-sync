package components.dbox;

import com.dropbox.core.v2.files.Metadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public interface DBoxApi {

    void upload(File file);

    ByteArrayOutputStream download(String name);

    List<Metadata> getFolderEntries();

}
