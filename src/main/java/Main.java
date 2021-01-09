import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import synchronizer.App;
import synchronizer.components.dbox.DBoxApi;
import synchronizer.components.dbox.DBoxComponent;
import synchronizer.components.dbox.hasher.DBoxHashHelper;
import synchronizer.components.dbox.hasher.DBoxHasher;
import synchronizer.components.file.FilesComponent;
import synchronizer.components.folder.FolderComponent;
import synchronizer.components.folder.FolderEventFunction;
import synchronizer.components.properties.AppProperties;
import synchronizer.components.properties.PropertiesComponent;
import synchronizer.components.util.DateUtilsComponent;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.StandardWatchEventKinds;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        AppProperties appProperties = new AppProperties(new PropertiesComponent(System.getProperty("properties")));

        DateUtilsComponent dateUtils = new DateUtilsComponent();

        DBoxHashHelper hashHelper = new DBoxHashHelper(new DBoxHasher());

        DBoxApi dBoxComponent = new DBoxComponent(
                dateUtils,
                new DbxClientV2(
                        DbxRequestConfig.newBuilder(appProperties.getClientIdentifier())
                                .withUserLocale(Locale.getDefault().toString())
                                .build(),
                        appProperties.getDBoxAuth()
                )
        );

        FolderEventFunction eventFunction = new FolderEventFunction(dBoxComponent, hashHelper);

        new App(
                dBoxComponent,
                new FilesComponent(hashHelper, dateUtils),
                new FolderComponent(
                        appProperties.getSyncFolder(),
                        Map.of(
                                StandardWatchEventKinds.ENTRY_CREATE, eventFunction::eventCreateFunction,
                                StandardWatchEventKinds.ENTRY_DELETE, eventFunction::eventDeleteFunction,
                                StandardWatchEventKinds.ENTRY_MODIFY, eventFunction::eventModifyFunction
                        )
                ),
                appProperties.getSyncTime()
        ).sync();

    }

}
