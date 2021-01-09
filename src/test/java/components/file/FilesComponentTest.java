package components.file;

import com.dropbox.core.v2.files.FileMetadata;
import components.dbox.hasher.DBoxHashHelper;
import components.dbox.hasher.DBoxHasher;
import components.file.FilesComponent;
import components.util.DateUtilsComponent;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FilesComponentTest {

    @Test
    public void testGetFilesForDownload() throws NoSuchAlgorithmException {
        FilesComponent filesComponent = new FilesComponent(new DBoxHashHelper(new DBoxHasher()), new DateUtilsComponent());
        assertThat(
                "Empty lists",
                filesComponent.getFilesForDownload(
                        Collections.emptyList(),
                        Collections.emptyList()
                ).isEmpty(),
                is(true)
        );
    }

    @Test
    public void testGetFilesForDownload2() throws NoSuchAlgorithmException {
        FilesComponent filesComponent = new FilesComponent(new DBoxHashHelper(new DBoxHasher()), new DateUtilsComponent());

        assertThat(
                "Has files in Dropbox, empty files local",
                filesComponent.getFilesForDownload(
                        Collections.singletonList(Paths.get("src/test/resources/scratches/1.md").toFile()),
                        Collections.emptyList()
                ),
                is(Collections.emptyList())
        );

    }

    @Test
    public void testGetFilesForDownload3() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();

        assertThat(
                "Same file",
                filesComponent.getFilesForDownload(
                        Collections.singletonList(file),
                        Collections.singletonList(
                                FileMetadata
                                        .newBuilder(file.getName(), "123", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
                                        .withContentHash(hashHelper.getHash(file))
                                        .build()
                        )
                ),
                is(Collections.emptyList())
        );
    }

    @Test
    public void testGetFilesForDownload4() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();
        List<FileMetadata> dbFolderEntries = Collections.singletonList(
                FileMetadata
                        .newBuilder(file.getName(), "123", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
                        .withContentHash(hashHelper.getHash(file))
                        .build()
        );
        assertThat(
                "File in storage",
                filesComponent.getFilesForDownload(
                        Collections.emptyList(),
                        dbFolderEntries
                ),
                is(dbFolderEntries)
        );
    }

    @Test
    public void testGetFilesForDownload5() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();
        List<FileMetadata> dbFolderEntries = Collections.singletonList(
                FileMetadata
                        .newBuilder(file.getName(), "123", new Date(file.lastModified() + 100000), new Date(file.lastModified() + 100000), "123123123", file.length())
                        .withContentHash(hashHelper.getHash(file).replace("a", "b"))
                        .build()
        );
        assertThat(
                "Same file, different hash and date",
                filesComponent.getFilesForDownload(
                        Collections.singletonList(file),
                        dbFolderEntries
                ),
                is(dbFolderEntries)
        );
    }

    @Test
    public void testGetFilesForDownload6() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();
        List<FileMetadata> dbFolderEntries = Collections.singletonList(
                FileMetadata
                        .newBuilder(file.getName(), "123", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
                        .withContentHash(hashHelper.getHash(file).replace("a", "b"))
                        .build()
        );
        assertThat(
                "Same file, different hash, same date",
                filesComponent.getFilesForDownload(
                        Collections.singletonList(file),
                        dbFolderEntries
                ),
                is(Collections.emptyList())
        );
    }

    @Test
    public void testGetFilesForUpload() throws NoSuchAlgorithmException {
        FilesComponent filesComponent = new FilesComponent(new DBoxHashHelper(new DBoxHasher()), new DateUtilsComponent());
        assertThat(
                "Empty lists",
                filesComponent.getFilesForUpload(
                        Collections.emptyList(),
                        Collections.emptyList()
                ).isEmpty(),
                is(true)
        );
    }

    @Test
    public void testGetFilesForUpload2() throws NoSuchAlgorithmException {
        FilesComponent filesComponent = new FilesComponent(new DBoxHashHelper(new DBoxHasher()), new DateUtilsComponent());
        List<File> folderFiles = Collections.singletonList(
                Paths.get("src/test/resources/scratches/1.md").toFile()
        );
        assertThat(
                "Has local files, empty in storage",
                filesComponent.getFilesForUpload(
                        folderFiles,
                        Collections.emptyList()
                ),
                is(folderFiles)
        );
    }

    @Test
    public void testGetFilesForUpload3() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();
        List<File> folderFiles = Collections.singletonList(file);
        assertThat(
                "Same file in storage",
                filesComponent.getFilesForUpload(
                        folderFiles,
                        Collections.singletonList(
                                FileMetadata
                                        .newBuilder(file.getName(), "123", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
                                        .withContentHash(hashHelper.getHash(file))
                                        .build()
                        )
                ),
                is(Collections.emptyList())
        );
    }

    @Test
    public void testGetFilesForUpload4() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/2.md").toFile();
        List<File> folderFiles = Collections.singletonList(
                Paths.get("src/test/resources/scratches/1.md").toFile()
        );
        assertThat(
                "Different file in storage",
                filesComponent.getFilesForUpload(
                        folderFiles,
                        Collections.singletonList(
                                FileMetadata
                                        .newBuilder(file.getName(), "321", new Date(file.lastModified()), new Date(file.lastModified()), "123123123", file.length())
                                        .withContentHash(hashHelper.getHash(file))
                                        .build()
                        )
                ),
                is(folderFiles)
        );
    }

    @Test
    public void testGetFilesForUpload5() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();
        List<File> folderFiles = Collections.singletonList(file);
        assertThat(
                "Same file, local is newer",
                filesComponent.getFilesForUpload(
                        folderFiles,
                        Collections.singletonList(
                                FileMetadata
                                        .newBuilder(file.getName(), "123", new Date(file.lastModified() - 100000), new Date(file.lastModified() - 100000), "123123123", file.length())
                                        .withContentHash(hashHelper.getHash(file).replace("a", "b"))
                                        .build()
                        )
                ),
                is(folderFiles)
        );
    }

    @Test
    public void testGetFilesForUpload6() throws NoSuchAlgorithmException {
        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());
        FilesComponent filesComponent = new FilesComponent(hashHelper, new DateUtilsComponent());
        File file = Paths.get("src/test/resources/scratches/1.md").toFile();
        List<File> folderFiles = Collections.singletonList(file);
        assertThat(
                "Same file, local is older",
                filesComponent.getFilesForUpload(
                        folderFiles,
                        Collections.singletonList(
                                FileMetadata
                                        .newBuilder(file.getName(), "123", new Date(file.lastModified() + 1000000), new Date(file.lastModified() + 1000000), "123123123", file.length())
                                        .withContentHash(hashHelper.getHash(file).replace("a", "b"))
                                        .build()
                        )
                ),
                is(Collections.emptyList())
        );
    }
}

