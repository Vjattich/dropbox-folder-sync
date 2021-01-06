package components;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import components.dbox.hasher.DBoxHashHelper;
import components.utils.DateUtilsComponent;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class FilesComponent {

    private final String folderPath;
    private final DBoxHashHelper hashHelper;
    private final DateUtilsComponent dateUtils;

    public FilesComponent(String folderPath, DBoxHashHelper hashHelper, DateUtilsComponent dateUtils) {
        this.folderPath = folderPath;
        this.hashHelper = hashHelper;
        this.dateUtils = dateUtils;
    }

    @SneakyThrows
    public void save(String fileName, Date clientModifiedDate, ByteArrayOutputStream byteArrayOutputStream) {
        String filePath = folderPath + File.separator + fileName;
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            byteArrayOutputStream.writeTo(outputStream);
            new File(filePath).setLastModified(clientModifiedDate.getTime());
        }
    }

    //todo this can be solve through good object
    public List<Metadata> getFilesForDownload(List<File> folderFiles, List<FileMetadata> dbFolderEntries) {
        Map<String, File> fileMap = getFileMap(folderFiles);
        return dbFolderEntries.stream().filter(metadata -> isMetadataDifferent(fileMap, metadata)).collect(toList());
    }

    private boolean isMetadataDifferent(Map<String, File> fileHexMap, FileMetadata fileMetadata) {
        File file = fileHexMap.get(fileMetadata.getName());
        return Objects.isNull(file)
                || !fileMetadata.getContentHash().equals(hashHelper.getHash(file))
                && dateUtils.toLocalDateTimeWithoutNano(fileMetadata.getClientModified()).isAfter(dateUtils.toLocalDateTimeWithoutNano(file.lastModified()));
    }

    public List<File> getFilesForUpload(List<File> folderFiles, List<FileMetadata> dbFolderEntries) {
        Map<String, FileMetadata> metadataHexMap = getMetadataMap(dbFolderEntries);
        return folderFiles.stream().filter(file -> isFileDifferent(metadataHexMap, file)).collect(toList());
    }

    private boolean isFileDifferent(Map<String, FileMetadata> metadataHexMap, File localFile) {
        FileMetadata fileMetadata = metadataHexMap.get(localFile.getName());
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
                                Metadata::getName,
                                metadata -> metadata
                        )
                );
    }

}
