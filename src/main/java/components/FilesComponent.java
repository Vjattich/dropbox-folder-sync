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
    public void save(String fileName, ByteArrayOutputStream byteArrayOutputStream) {
        try (OutputStream outputStream = new FileOutputStream(folderPath + File.separator + fileName)) {
            byteArrayOutputStream.writeTo(outputStream);
        }
    }

    //todo this can be solve through good object
    public List<Metadata> getFilesForDownload(List<File> folderFiles, List<Metadata> dbFolderEntries) {
        Map<String, File> fileHexMap = getFileMap(folderFiles);
        return dbFolderEntries.stream().filter(metadata -> isMetadataDifferent(fileHexMap, metadata)).collect(toList());
    }

    private boolean isMetadataDifferent(Map<String, File> fileHexMap, Metadata metadata) {
        FileMetadata fileMetadata = (FileMetadata) metadata;
        File file = fileHexMap.get(metadata.getName());
        return Objects.isNull(file)
                || !fileMetadata.getContentHash().equals(hashHelper.getHash(file))
                && dateUtils.toLocalDateTimeWithoutNano(fileMetadata.getClientModified()).isAfter(dateUtils.toLocalDateTimeWithoutNano(file.lastModified()));
    }

    public List<File> getFilesForUpload(List<File> folderFiles, List<Metadata> dbFolderEntries) {
        Map<String, Metadata> metadataHexMap = getMetadataMap(dbFolderEntries);
        return folderFiles.stream().filter(file -> isFileDifferent(metadataHexMap, file)).collect(toList());
    }

    private boolean isFileDifferent(Map<String, Metadata> metadataHexMap, File localFile) {
        Metadata metadata = metadataHexMap.get(localFile.getName());

        if (Objects.isNull(metadata)) {
            return true;
        }

        FileMetadata fileMetadata = (FileMetadata) metadata;

        return !hashHelper.getHash(localFile).equals(fileMetadata.getContentHash())
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

    private Map<String, Metadata> getMetadataMap(List<Metadata> dbFolderEntries) {
        return dbFolderEntries.stream()
                .collect(
                        toMap(
                                Metadata::getName,
                                metadata -> metadata
                        )
                );
    }

}
