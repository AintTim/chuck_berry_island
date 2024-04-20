package entities.plants;

import entities.Entity;

public abstract class Plant extends Entity {
    protected Plant(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
}
