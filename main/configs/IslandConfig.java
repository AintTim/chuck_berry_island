package configs;

import constants.Setting;
import lombok.Getter;

import java.util.Properties;

@Getter
public class IslandConfig {
    private final int width;
    private final int height;
    private final int lifeSpan;

    public IslandConfig(Properties properties) {
        this.width = Integer.parseInt(properties.getProperty(Setting.WIDTH.getProperty()));
        this.height = Integer.parseInt(properties.getProperty(Setting.HEIGHT.getProperty()));
        this.lifeSpan = Integer.parseInt(properties.getProperty(Setting.LIFESPAN.getProperty()));
    }
}
