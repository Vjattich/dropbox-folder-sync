import com.dropbox.core.v2.files.Metadata;
import components.DBoxComponent;
import components.FilesComponent;
import components.FolderComponent;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    private final DBoxComponent dBoxComponent;
    private final FilesComponent filesComponent;
    private final FolderComponent folderComponent;
    private final long updateMilliseconds;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public App(DBoxComponent dBoxComponent,
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
    }

    @SneakyThrows
    private void syncFiles() {

        folderComponent.stopWatch();

        List<File> files = folderComponent.getFiles();

        List<Metadata> dbFolderEntries = dBoxComponent.getFolderEntries();

        System.out.println("start sync");

        //check what we upload
        uploadFiles(files, dbFolderEntries);

        //or download
        downloadFiles(files, dbFolderEntries);

        folderComponent.resumeWatch();

        System.out.println("end sync");
    }

    private void uploadFiles(List<File> listFiles, List<Metadata> dbFolderEntries) {
        for (File file : filesComponent.getFilesForUpload(listFiles, dbFolderEntries)) {
            dBoxComponent.upload(file);
        }
    }

    private void downloadFiles(List<File> files, List<Metadata> dbFolderEntries) {
        for (Metadata file : filesComponent.getFilesForDownload(files, dbFolderEntries)) {
            String name = file.getName();
            filesComponent.save(name, dBoxComponent.download(name));
        }
    }

}
