package configs;

import lombok.Getter;

import java.util.*;

@Getter
public class IslandConfig {
    private int width;
    private int height;
    private int lifeSpan;

    public IslandConfig(Properties properties) {
        this.width = Integer.parseInt(properties.getProperty("island.width"));
        this.height = Integer.parseInt(properties.getProperty("island.height"));
        this.lifeSpan = Integer.parseInt(properties.getProperty("island.lifespan"));
    }
}
