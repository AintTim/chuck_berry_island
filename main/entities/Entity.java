package entities;

import lombok.Getter;

@Getter
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
}
