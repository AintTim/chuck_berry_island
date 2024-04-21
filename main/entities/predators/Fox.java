package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Fox extends Predator{
    public Fox(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Fox(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }
}
