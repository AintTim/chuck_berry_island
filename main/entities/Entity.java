package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
