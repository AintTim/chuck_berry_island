package entities.predators;

import entities.Animal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Predator extends Animal {
    protected Predator(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
}
