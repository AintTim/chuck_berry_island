package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Eagle extends Predator{
    public Eagle(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Eagle(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }
}
