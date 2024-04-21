package entities.plants;

import entities.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Grass extends Plant{
    public Grass(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Grass(Entity entity) {
        super(entity.getWeight(), entity.getVelocity(), entity.getLimit(), entity.getHunger());
    }
}
