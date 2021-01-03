import components.*;

import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class Main {

    public static void main(String[] args) {

        PropertiesComponent propComponent = new PropertiesComponent();

        DBoxComponent dBoxComponent = new DBoxComponent(propComponent.get("dbox.oauth2"), propComponent.get("dbox.folder"));

        EventFunctionComponent eventFunctionComponent = new EventFunctionComponent(dBoxComponent);

        new App(
                dBoxComponent,
                new FilesComponent(
                        propComponent.get("sync.folder"),
                        new DBoxHasher()
                ),
                new FolderComponent(
                        propComponent.get("sync.folder"),
                        Map.of(
                                ENTRY_CREATE, eventFunctionComponent::eventCreateFunction,
                                ENTRY_DELETE, eventFunctionComponent::eventDeleteFunction,
                                ENTRY_MODIFY, eventFunctionComponent::eventModifyFunction
                        )
                ),
                (long) propComponent.get("sync.time", long.class)
        ).sync();

    }

}
