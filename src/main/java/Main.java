import components.*;
import components.hasher.DBoxHashHelper;
import components.hasher.DBoxHasher;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.StandardWatchEventKinds;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        PropertiesComponent propComponent = new PropertiesComponent("src/main/resources/application.properties");

        DateUtilsComponent dateUtils = new DateUtilsComponent();

        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());

        DBoxComponent dBoxComponent = new DBoxComponent(dateUtils, propComponent.get("dbox.oauth2"), propComponent.get("dbox.client.identifier"));

        EventFunctionComponent eventFunctionComponent = new EventFunctionComponent(dBoxComponent, hashHelper);

        new App(
                dBoxComponent,
                new FilesComponent(
                        propComponent.get("sync.folder"),
                        hashHelper,
                        dateUtils
                ),
                new FolderComponent(
                        propComponent.get("sync.folder"),
                        Map.of(
                                StandardWatchEventKinds.ENTRY_CREATE, path -> eventFunctionComponent.eventCreateFunction(path),
                                StandardWatchEventKinds.ENTRY_DELETE, path -> eventFunctionComponent.eventDeleteFunction(path),
                                StandardWatchEventKinds.ENTRY_MODIFY, path -> eventFunctionComponent.eventModifyFunction(path)
                        )
                ),
                (long) propComponent.get("sync.time", long.class)
        ).sync();

    }

}
