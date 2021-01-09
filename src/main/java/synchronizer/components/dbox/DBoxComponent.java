package synchronizer.components.dbox;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import synchronizer.components.util.DateUtilsComponent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DBoxComponent implements DBoxApi {

    private final DateUtilsComponent dateUtils;

    private final DbxClientV2 client;

    public DBoxComponent(DateUtilsComponent dateUtils, DbxClientV2 client) {
        this.dateUtils = dateUtils;
        this.client = client;
    }

    @Override
    @SneakyThrows
    //todo mute version
    public void upload(File file) {

        log.info("upload file {} to dropbox", file.getName());

        client.files()
                .uploadBuilder("/" + file.getName())
                .withClientModified(dateUtils.toDateWithoutNano(file.lastModified()))
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(new FileInputStream(file));
    }

    @Override
    @SneakyThrows
    public ByteArrayOutputStream download(String name) {

        log.info("download file {} from dropbox", name);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.files().downloadBuilder("/" + name).download(out);
        return out;
    }

    @Override
    @SneakyThrows
    public List<FileMetadata> getFolderEntries() {
        return client.files()
                .listFolder("")
                .getEntries()
                .stream()
                .map(metadata -> (FileMetadata) metadata)
                .collect(Collectors.toList());
    }

}
