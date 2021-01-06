package components;

import com.dropbox.core.v2.files.FileMetadata;
import components.dbox.DBoxApi;
import components.dbox.hasher.DBoxHashHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EventFunctionComponent {

    private final DBoxApi dBoxComponent;
    private final DBoxHashHelper hashHelper;

    public EventFunctionComponent(DBoxApi dBoxComponent, DBoxHashHelper hashHelper) {
        this.dBoxComponent = dBoxComponent;
        this.hashHelper = hashHelper;
    }

    public Path eventCreateFunction(Path path) {

        log.info("created new file in " + path);

        File file = path.toFile();

        if (isAlreadyInDBox(file)) {
            return path;
        }

        dBoxComponent.upload(file);

        return path;
    }

    public Path eventModifyFunction(Path path) {

        log.info("modifying file in {}", path);

        File file = path.toFile();

        if (isAlreadyInDBox(file)) {
            return path;
        }

        dBoxComponent.upload(file);

        return path;
    }

    public Path eventDeleteFunction(Path path) {
        log.info("deleted file {} ", path);
        //todo delete event
        return path;
    }

    private boolean isAlreadyInDBox(File file) {

        Map<String, String> folderEntries = dBoxComponent.getFolderEntries().stream()
                .collect(
                        Collectors.toMap(
                                FileMetadata::getName,
                                FileMetadata::getContentHash
                        )
                );

        boolean b = folderEntries.containsKey(file.getName()) && folderEntries.get(file.getName()).equals(hashHelper.getHash(file));

        if (b) {
            log.info("file {} is already in dropbox", file.getPath());
        }

        return b;
    }

}
