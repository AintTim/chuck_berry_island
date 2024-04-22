package entities.predators;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.Creator;

@NoArgsConstructor
public class Snake extends Predator implements Creator<Snake> {
    public Snake(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Snake(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Snake create(Entity entity) {
        return new Snake(entity);
    }
}
