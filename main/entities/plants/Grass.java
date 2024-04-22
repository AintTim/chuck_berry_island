package entities.plants;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.Creator;

@NoArgsConstructor
public class Grass extends Plant implements Creator<Grass> {
    public Grass(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Grass(Entity entity) {
        super(entity.getWeight(), entity.getVelocity(), entity.getLimit(), entity.getHunger());
    }

    @Override
    public Grass create(Entity entity) {
        return new Grass(entity);
    }
}
