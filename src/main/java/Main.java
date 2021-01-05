import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import components.EventFunctionComponent;
import components.FilesComponent;
import components.FolderComponent;
import components.PropertiesComponent;
import components.dbox.DBoxApi;
import components.dbox.DBoxComponent;
import components.dbox.hasher.DBoxHashHelper;
import components.dbox.hasher.DBoxHasher;
import components.utils.DateUtilsComponent;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.StandardWatchEventKinds;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        PropertiesComponent propComponent = new PropertiesComponent("src/main/resources/application.properties");

        DateUtilsComponent dateUtils = new DateUtilsComponent();

        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());

        DBoxApi dBoxComponent = new DBoxComponent(
                dateUtils,
                new DbxClientV2(
                        DbxRequestConfig.newBuilder(propComponent.get("dbox.client.identifier"))
                                .withUserLocale(Locale.getDefault().toString())
                                .build(),
                        propComponent.get("dbox.oauth2")
                )
        );

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
