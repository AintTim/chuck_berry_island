package entities.herbivores;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public class Sheep extends Herbivore implements CreatorService<Sheep> {
    public Sheep(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
    public Sheep(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Sheep create(Entity entity) {
        return new Sheep(entity);
    }
}
