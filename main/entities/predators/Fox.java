package entities.predators;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public class Fox extends Predator implements CreatorService<Fox> {
    public Fox(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Fox(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Fox create(Entity entity) {
        return new Fox(entity);
    }
}
