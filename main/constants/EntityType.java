package constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Entity;
import entities.herbivores.*;
import entities.plants.Grass;
import entities.predators.*;

import java.util.Arrays;

public enum EntityType {
    BEAR("bear", Bear.class),
    CATERPILLAR("caterpillar", Caterpillar.class),
    DEER("deer", Deer.class),
    DUCK("duck", Duck.class),
    EAGLE("eagle", Eagle.class),
    FOX("fox", Fox.class),
    GRASS("grass", Grass.class),
    HORSE("horse", Horse.class),
    MOUSE("mouse", Mouse.class),
    RABBIT("rabbit", Rabbit.class),
    SHEEP("sheep", Sheep.class),
    SNAKE("snake", Snake.class),
    WOLF("wolf",Wolf.class);

    private final String name;
    private final Class<? extends Entity> clazz;

    EntityType(String name, Class<? extends Entity> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public Class<? extends Entity> getObjectClass() {
        return this.clazz;
    }

    public static EntityType ofClass(Class<? extends Entity> clazz) {
        return Arrays.stream(EntityType.values())
                .filter(e -> e.getObjectClass().equals(clazz))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Искомая сущность отсутствует в списке"));
    }

    @JsonProperty(value = "value")
    @JsonCreator
    public static EntityType forValue(String value) {
        return EntityType.valueOf(value.toUpperCase());
    }
}
