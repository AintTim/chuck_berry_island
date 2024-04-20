package entities.predators;

import entities.Animal;

public abstract class Predator extends Animal {
    protected Predator(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
}
