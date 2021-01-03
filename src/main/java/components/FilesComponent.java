package components;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import lombok.SneakyThrows;

import java.io.*;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FilesComponent {

    private final String folderPath;
    private final DBoxHasher hasher;

    public FilesComponent(String folderPath, DBoxHasher hasher) {
        this.folderPath = folderPath;
        this.hasher = hasher;
    }

    @SneakyThrows
    public void save(String fileName, ByteArrayOutputStream byteArrayOutputStream) {
        try (OutputStream outputStream = new FileOutputStream(folderPath + File.separator + fileName)) {
            byteArrayOutputStream.writeTo(outputStream);
        }
    }

    public List<Metadata> getFilesForDownload(List<File> folderFiles, List<Metadata> dbFolderEntries) {

        Map<String, String> fileHexMap = getFileHexMap(folderFiles);

        List<Metadata> list = new ArrayList<>();
        for (Metadata metadata : dbFolderEntries) {
            if (!((FileMetadata) metadata).getContentHash().equals(fileHexMap.get(metadata.getName()))) {
                list.add(metadata);
            }
        }

        return list;
    }

    public List<File> getFilesForUpload(List<File> folderFiles, List<Metadata> dbFolderEntries) {

        Map<String, String> metadataHexMap = getMetadataHexMap(dbFolderEntries);

        List<File> list = new ArrayList<>();
        for (File file : folderFiles) {
            if (!toHex(file).equals(metadataHexMap.get(file.getName()))) {
                list.add(file);
            }
        }

        return list;
    }

    private Map<String, String> getFileHexMap(List<File> folderFiles) {
        return folderFiles.stream()
                .collect(
                        Collectors.toMap(
                                File::getName,
                                this::toHex
                        )
                );
    }

    private Map<String, String> getMetadataHexMap(List<Metadata> dbFolderEntries) {
        return dbFolderEntries.stream()
                .collect(
                        Collectors.toMap(
                                Metadata::getName,
                                metadata -> ((FileMetadata) metadata).getContentHash()
                        )
                );
    }

    @SneakyThrows
    String toHex(File file) {

        byte[] buf = new byte[1024];

        InputStream in = new FileInputStream(file);
        try {
            while (true) {
                int n = in.read(buf);
                if (n < 0) {
                    break;// EOF
                }
                hasher.update(buf, 0, n);
            }
        } finally {
            in.close();
        }

        return DBoxHasher.hex(hasher.digest());
    }

}
