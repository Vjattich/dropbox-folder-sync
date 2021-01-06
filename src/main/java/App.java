import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import components.dbox.DBoxApi;
import components.FilesComponent;
import components.FolderComponent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class App {

    private final DBoxApi dBoxComponent;
    private final FilesComponent filesComponent;
    private final FolderComponent folderComponent;
    private final long updateMilliseconds;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public App(DBoxApi dBoxComponent,
               FilesComponent filesComponent,
               FolderComponent folderComponent,
               long updateMilliseconds) {
        this.dBoxComponent = dBoxComponent;
        this.filesComponent = filesComponent;
        this.folderComponent = folderComponent;
        this.updateMilliseconds = updateMilliseconds;
    }

    public void sync() {

        scheduler.scheduleAtFixedRate(
                this::syncFiles,
                1,
                updateMilliseconds,
                TimeUnit.MILLISECONDS
        );

        folderComponent.startWatch();

    }

    @SneakyThrows
    private void syncFiles() {

        List<File> files = folderComponent.getFiles();

        List<FileMetadata> dbFolderEntries = dBoxComponent.getFolderEntries();

        log.info("start sync phase");

        //check what we upload
        uploadFiles(files, dbFolderEntries);

        //or download
        downloadFiles(files, dbFolderEntries);

        log.info("end sync phase");
    }

    private void uploadFiles(List<File> listFiles, List<FileMetadata> dbFolderEntries) {
        for (File file : filesComponent.getFilesForUpload(listFiles, dbFolderEntries)) {
            dBoxComponent.upload(file);
        }
    }

    private void downloadFiles(List<File> files, List<FileMetadata> dbFolderEntries) {
        for (Metadata metadata : filesComponent.getFilesForDownload(files, dbFolderEntries)) {
            String name = metadata.getName();
            filesComponent.save(name, ((FileMetadata) metadata).getClientModified() ,dBoxComponent.download(name));
        }
    }

}
