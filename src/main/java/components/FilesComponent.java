package components;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import components.hasher.DboxHashHelper;
import lombok.SneakyThrows;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class FilesComponent {

    private final String folderPath;
    private final DboxHashHelper hashHelper;

    public FilesComponent(String folderPath, DboxHashHelper hashHelper) {
        this.folderPath = folderPath;
        this.hashHelper = hashHelper;
    }

    @SneakyThrows
    public void save(String fileName, ByteArrayOutputStream byteArrayOutputStream) {
        try (OutputStream outputStream = new FileOutputStream(folderPath + File.separator + fileName)) {
            byteArrayOutputStream.writeTo(outputStream);
        }
    }

    //todo this can be solve trought good object
    public List<Metadata> getFilesForDownload(List<File> folderFiles, List<Metadata> dbFolderEntries) {
        Map<String, String> fileHexMap = getFileHexMap(folderFiles);
        return dbFolderEntries.stream().filter(metadata -> isMetadataDifferent(fileHexMap, metadata)).collect(toList());
    }

    private boolean isMetadataDifferent(Map<String, String> fileHexMap, Metadata metadata) {
        return !((FileMetadata) metadata).getContentHash().equals(fileHexMap.get(metadata.getName()));
    }

    public List<File> getFilesForUpload(List<File> folderFiles, List<Metadata> dbFolderEntries) {
        Map<String, String> metadataHexMap = getMetadataHexMap(dbFolderEntries);
        return folderFiles.stream().filter(file -> isFileDifferent(metadataHexMap, file)).collect(toList());
    }

    private boolean isFileDifferent(Map<String, String> metadataHexMap, File file) {
        return !hashHelper.getHash(file).equals(metadataHexMap.get(file.getName()));
    }

    private Map<String, String> getFileHexMap(List<File> folderFiles) {
        return folderFiles.stream()
                .collect(
                        toMap(
                                File::getName,
                                hashHelper::getHash
                        )
                );
    }

    private Map<String, String> getMetadataHexMap(List<Metadata> dbFolderEntries) {
        return dbFolderEntries.stream()
                .collect(
                        toMap(
                                Metadata::getName,
                                metadata -> ((FileMetadata) metadata).getContentHash()
                        )
                );
    }

}
