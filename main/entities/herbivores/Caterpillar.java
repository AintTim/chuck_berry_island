package entities.herbivores;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public class Caterpillar extends Herbivore implements CreatorService<Caterpillar> {
    public Caterpillar(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Caterpillar(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Caterpillar create(Entity entity) {
        return new Caterpillar(entity);
    }
}
