package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;
import services.Creator;

@NoArgsConstructor
public class Duck extends Herbivore implements Creator<Duck> {
    public Duck(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Duck(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }

    @Override
    public Duck create(Entity entity) {
        return new Duck(entity);
    }
}
