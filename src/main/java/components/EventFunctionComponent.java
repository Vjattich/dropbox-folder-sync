package components;

import java.io.File;
import java.nio.file.Path;

public class EventFunctionComponent {

    private final DBoxComponent dBoxComponent;

    public EventFunctionComponent(DBoxComponent dBoxComponent) {
        this.dBoxComponent = dBoxComponent;
    }

    public Path eventCreateFunction(Path path) {

        System.out.println("create file " + path);

        File file = path.toFile();

        if (file.exists()) {
            dBoxComponent.upload(file);
        }

        return path;
    }

    public Path eventDeleteFunction(Path path) {
        System.out.println("delete file " + path);
        //todo delete event
        return path;
    }

    public Path eventModifyFunction(Path path) {

        System.out.println("modify file " + path);

        File file = path.toFile();

        if (file.exists()) {
            dBoxComponent.upload(file);
        }

        return path;
    }

}
