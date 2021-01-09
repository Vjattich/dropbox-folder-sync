package synchronizer.components.file;

import com.dropbox.core.v2.files.FileMetadata;
import synchronizer.components.dbox.hasher.DBoxHashHelper;
import synchronizer.components.util.DateUtilsComponent;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class FilesComponent {

    private final DBoxHashHelper hashHelper;
    private final DateUtilsComponent dateUtils;

    public FilesComponent(DBoxHashHelper hashHelper, DateUtilsComponent dateUtils) {
        this.hashHelper = hashHelper;
        this.dateUtils = dateUtils;
    }

    //todo this can be solve through good object
    public List<FileMetadata> getFilesForDownload(List<File> folderFiles, List<FileMetadata> dbFolderEntries) {
        Map<String, File> fileMap = getFileMap(folderFiles);
        return dbFolderEntries.stream().filter(metadata -> isMetadataDifferent(fileMap, metadata)).collect(toList());
    }

    private boolean isMetadataDifferent(Map<String, File> fileMap, FileMetadata fileMetadata) {
        File file = fileMap.get(fileMetadata.getName());
        return Objects.isNull(file)
                || !fileMetadata.getContentHash().equals(hashHelper.getHash(file))
                && dateUtils.toLocalDateTimeWithoutNano(fileMetadata.getClientModified()).isAfter(dateUtils.toLocalDateTimeWithoutNano(file.lastModified()));
    }

    public List<File> getFilesForUpload(List<File> folderFiles, List<FileMetadata> dbFolderEntries) {
        Map<String, FileMetadata> metadataMap = getMetadataMap(dbFolderEntries);
        return folderFiles.stream().filter(file -> isFileDifferent(metadataMap, file)).collect(toList());
    }

    private boolean isFileDifferent(Map<String, FileMetadata> metadataMap, File localFile) {
        FileMetadata fileMetadata = metadataMap.get(localFile.getName());
        return Objects.isNull(fileMetadata)
                || !hashHelper.getHash(localFile).equals(fileMetadata.getContentHash())
                && dateUtils.toLocalDateTimeWithoutNano(localFile.lastModified()).isAfter(dateUtils.toLocalDateTimeWithoutNano(fileMetadata.getClientModified()));
    }

    private Map<String, File> getFileMap(List<File> folderFiles) {
        return folderFiles.stream()
                .collect(
                        toMap(
                                File::getName,
                                file -> file
                        )
                );
    }

    private Map<String, FileMetadata> getMetadataMap(List<FileMetadata> dbFolderEntries) {
        return dbFolderEntries.stream()
                .collect(
                        toMap(
                                FileMetadata::getName,
                                metadata -> metadata
                        )
                );
    }

}
