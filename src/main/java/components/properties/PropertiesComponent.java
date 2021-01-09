package components.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class PropertiesComponent {

    private final Properties properties = new Properties();

    public PropertiesComponent(String propertiesPath) {
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with properties init", e);
        }
    }

    public String get(String key) {
        Objects.requireNonNull(key);
        return properties.getProperty(key);
    }

    public Object get(String key, Class<?> type) {

        Objects.requireNonNull(key);
        Objects.requireNonNull(type);

        String property = properties.getProperty(key);

        if (type == String.class)
            return property;
        if (type == boolean.class)
            return Boolean.parseBoolean(property);
        if (type == int.class)
            return Integer.parseInt(property);
        if (type == float.class)
            return Float.parseFloat(property);
        if (type == long.class)
            return Long.parseLong(property);

        throw new IllegalArgumentException("Unknown configuration value type: " + type.getName());
    }

}
