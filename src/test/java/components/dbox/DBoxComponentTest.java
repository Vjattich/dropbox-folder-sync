package components.dbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.DownloadBuilder;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadBuilder;
import components.util.DateUtilsComponent;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

//todo is a stupid test, it is all of mock
public class DBoxComponentTest {

    @Test
    public void testUpload() throws IOException, DbxException {
        File file = new File("src/test/resources/scratches/1.md");

        DbxClientV2 clientV2mock = Mockito.mock(DbxClientV2.class);

        DbxUserFilesRequests filesRequestsMock = Mockito.mock(DbxUserFilesRequests.class);

        Mockito.when(clientV2mock.files()).thenReturn(filesRequestsMock);

        UploadBuilder uploadBuilderMock = Mockito.mock(UploadBuilder.class);

        Mockito.when(filesRequestsMock.uploadBuilder(any())).thenReturn(uploadBuilderMock);

        Mockito.when(uploadBuilderMock.withMode(any())).thenReturn(uploadBuilderMock);

        Mockito.when(uploadBuilderMock.withClientModified(any())).thenReturn(uploadBuilderMock);

        Mockito.when(uploadBuilderMock.uploadAndFinish(any())).thenReturn(
                new FileMetadata("1.md", "1", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
        );

        DBoxComponent dBoxComponent = new DBoxComponent(new DateUtilsComponent(), clientV2mock);
        dBoxComponent.upload(file);
    }

    @Test
    public void testDownload() throws DbxException, IOException {

        DbxClientV2 clientV2mock = Mockito.mock(DbxClientV2.class);

        DbxUserFilesRequests filesRequestsMock = Mockito.mock(DbxUserFilesRequests.class);

        Mockito.when(clientV2mock.files()).thenReturn(filesRequestsMock);

        DownloadBuilder downloadBuilderMock = Mockito.mock(DownloadBuilder.class);

        Mockito.when(filesRequestsMock.downloadBuilder(any())).thenReturn(downloadBuilderMock);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File file = new File("src/test/resources/scratches/1.md");
        Mockito.when(downloadBuilderMock.download(out)).thenReturn(
                new FileMetadata("1.md", "1", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
        );

        DBoxComponent dBoxComponent = new DBoxComponent(new DateUtilsComponent(), clientV2mock);
        dBoxComponent.download("1.md");
    }

}

