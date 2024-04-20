package entities.herbivores;

import entities.Animal;

public abstract class Herbivore extends Animal {
    protected Herbivore(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
}
