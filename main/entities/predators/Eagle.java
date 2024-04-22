package entities.predators;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public class Eagle extends Predator implements CreatorService<Eagle> {
    public Eagle(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Eagle(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Eagle create(Entity entity) {
        return new Eagle(entity);
    }
}
