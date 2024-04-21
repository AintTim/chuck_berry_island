package entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entities.herbivores.*;
import entities.plants.Grass;
import entities.predators.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bear.class, name = "bear"),
        @JsonSubTypes.Type(value = Caterpillar.class, name = "caterpillar"),
        @JsonSubTypes.Type(value = Deer.class, name = "deer"),
        @JsonSubTypes.Type(value = Duck.class, name = "duck"),
        @JsonSubTypes.Type(value = Eagle.class, name = "eagle"),
        @JsonSubTypes.Type(value = Fox.class, name = "fox"),
        @JsonSubTypes.Type(value = Grass.class, name = "grass"),
        @JsonSubTypes.Type(value = Horse.class, name = "horse"),
        @JsonSubTypes.Type(value = Mouse.class, name = "mouse"),
        @JsonSubTypes.Type(value = Rabbit.class, name = "rabbit"),
        @JsonSubTypes.Type(value = Sheep.class, name = "sheep"),
        @JsonSubTypes.Type(value = Snake.class, name = "snake"),
        @JsonSubTypes.Type(value = Wolf.class, name = "wolf"),
})
@Getter
@NoArgsConstructor
public abstract class Entity {
    protected Double weight;
    protected Integer velocity;
    protected Integer limit;
    protected Double hunger;

    protected Entity(Double weight, Integer velocity, Integer limit, Double hunger) {
        this.weight = weight;
        this.velocity = velocity;
        this.limit = limit;
        this.hunger = hunger;
    }

    protected Entity(Entity entity) {
        this(entity.getWeight(), entity.getVelocity(), entity.getLimit(), entity.getHunger());
    }
}
