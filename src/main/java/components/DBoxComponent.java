package components;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

public class DBoxComponent {

    private final DbxClientV2 client;

    private final DateUtilsComponent dateUtils;

    public DBoxComponent(String oauth2, DateUtilsComponent dateUtils) {
        this.client = new DbxClientV2(
                DbxRequestConfig.newBuilder("vjzettlr/1.0")
                        .withUserLocale(Locale.getDefault().toString())
                        .build(),
                oauth2
        );
        this.dateUtils = dateUtils;
    }

    @SneakyThrows
    //todo mute version
    public void upload(File file) {
        System.out.println("upload " + file.getName());
        client.files()
                .uploadBuilder("/" + file.getName())
                .withClientModified(dateUtils.toDateWithoutNano(file.lastModified()))
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(new FileInputStream(file));
    }

    @SneakyThrows
    public ByteArrayOutputStream download(String name) {
        System.out.println("download " + name);
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
