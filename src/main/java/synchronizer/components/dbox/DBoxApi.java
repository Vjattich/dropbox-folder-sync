package synchronizer.components.dbox;

import com.dropbox.core.v2.files.FileMetadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public interface DBoxApi {

    void upload(File file);

    ByteArrayOutputStream download(String name);

    List<FileMetadata> getFolderEntries();

}
