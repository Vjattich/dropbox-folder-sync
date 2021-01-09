package synchronizer.components.properties;

public class AppProperties {

    private final PropertiesComponent propComponent;

    public AppProperties(PropertiesComponent propertiesComponent) {
        this.propComponent = propertiesComponent;
    }


    public String getClientIdentifier() {
        return propComponent.get("dbox.client.identifier");
    }

    public String getDBoxAuth() {
        return propComponent.get("dbox.oauth2");
    }

    public String getSyncFolder() {
        return propComponent.get("sync.folder");
    }

    public long getSyncTime() {
        return (long) propComponent.get("sync.time", long.class);
    }

}
