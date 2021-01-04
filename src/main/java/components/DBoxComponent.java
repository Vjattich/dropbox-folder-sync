package components;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Locale;

@Slf4j
public class DBoxComponent {

    private final DateUtilsComponent dateUtils;

    private final DbxClientV2 client;

    public DBoxComponent(DateUtilsComponent dateUtils, String oauth2, String clientIdentifier) {
        this.dateUtils = dateUtils;
        this.client = new DbxClientV2(
                DbxRequestConfig.newBuilder(clientIdentifier)
                        .withUserLocale(Locale.getDefault().toString())
                        .build(),
                oauth2
        );
    }

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

    @SneakyThrows
    public ByteArrayOutputStream download(String name) {

        log.info("download file {} from dropbox", name);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.files().downloadBuilder("/" + name).download(out);
        return out;
    }

    @SneakyThrows
    public List<Metadata> getFolderEntries() {
        return client.files()
                .listFolder("")
                .getEntries();
    }

}
