package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;
import services.Creator;

@NoArgsConstructor
public class Deer extends Herbivore implements Creator<Deer> {
    public Deer(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Deer(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Deer create(Entity entity) {
        return new Deer(entity);
    }
}
