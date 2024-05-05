package constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Entity;
import entities.herbivores.*;
import entities.plants.Grass;
import entities.predators.*;

import java.util.Arrays;

public enum EntityType {
    BEAR(Bear.class),
    CATERPILLAR(Caterpillar.class),
    DEER(Deer.class),
    DUCK(Duck.class),
    EAGLE(Eagle.class),
    FOX(Fox.class),
    GRASS(Grass.class),
    HORSE(Horse.class),
    MOUSE(Mouse.class),
    RABBIT(Rabbit.class),
    SHEEP(Sheep.class),
    SNAKE(Snake.class),
    WOLF(Wolf.class);

    private final Class<? extends Entity> clazz;

    EntityType(Class<? extends Entity> clazz) {
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
