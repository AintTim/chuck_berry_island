package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Wolf extends Predator{
    public Wolf(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Wolf(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
    }
}