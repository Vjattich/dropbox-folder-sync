package components;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;

@Slf4j
public class EventFunctionComponent {

    private final DBoxComponent dBoxComponent;

    public EventFunctionComponent(DBoxComponent dBoxComponent) {
        this.dBoxComponent = dBoxComponent;
    }

    public Path eventCreateFunction(Path path) {

        log.info("created new file in " + path);

        File file = path.toFile();

        if (file.exists()) {
            dBoxComponent.upload(file);
        }

        return path;
    }

    public Path eventModifyFunction(Path path) {

        log.info("modifying file in " + path);

        File file = path.toFile();

        if (file.exists()) {
            dBoxComponent.upload(file);
        }

        return path;
    }

    public Path eventDeleteFunction(Path path) {
        log.info("deleted file in " + path);
        //todo delete event
        return path;
    }

}
