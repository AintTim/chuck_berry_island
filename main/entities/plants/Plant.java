package entities.plants;

import entities.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Plant extends Entity {
    protected Plant(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
}
