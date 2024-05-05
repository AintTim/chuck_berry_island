package handlers;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Getter
public class PropertiesHandler {
    Properties properties;

    public PropertiesHandler(Path properties) {
        this.properties = new Properties();
        initProperties(properties, this.properties);
    }

    private static void initProperties(Path path, Properties properties) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Искомый файл не существует по указанному пути");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getNumberProperty(String name) {
        return Integer.parseInt( properties.getProperty(name));
    }

    public Path getPath(String name) {
        return Path.of(properties.getProperty(name));
    }
}
