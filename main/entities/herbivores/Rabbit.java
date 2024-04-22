package entities.herbivores;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public class Rabbit extends Herbivore implements CreatorService<Rabbit> {
    public Rabbit(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Rabbit(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Rabbit create(Entity entity) {
        return new Rabbit(entity);
    }
}
