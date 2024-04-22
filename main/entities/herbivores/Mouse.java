package entities.herbivores;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public class Mouse extends Herbivore implements CreatorService<Mouse> {
    public Mouse(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Mouse(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Mouse create(Entity entity) {
        return new Mouse(entity);
    }
}
