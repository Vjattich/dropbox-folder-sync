import components.*;
import components.hasher.DBoxHasher;
import components.hasher.DboxHashHelper;

import java.nio.file.StandardWatchEventKinds;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        PropertiesComponent propComponent = new PropertiesComponent("src/main/resources/application.properties");

        DateUtilsComponent dateUtils = new DateUtilsComponent();

        DBoxComponent dBoxComponent = new DBoxComponent(propComponent.get("dbox.oauth2"), dateUtils);

        EventFunctionComponent eventFunctionComponent = new EventFunctionComponent(dBoxComponent);

        new App(
                dBoxComponent,
                new FilesComponent(
                        propComponent.get("sync.folder"),
                        new DboxHashHelper(new DBoxHasher())
                ),
                new FolderComponent(
                        propComponent.get("sync.folder"),
                        Map.of(
                                StandardWatchEventKinds.ENTRY_CREATE, path -> eventFunctionComponent.eventCreateFunction(path),
                                StandardWatchEventKinds.ENTRY_DELETE, path1 -> eventFunctionComponent.eventDeleteFunction(path1),
                                StandardWatchEventKinds.ENTRY_MODIFY, path2 -> eventFunctionComponent.eventModifyFunction(path2)
                        )
                ),
                (long) propComponent.get("sync.time", long.class)
        ).sync();

    }

}
