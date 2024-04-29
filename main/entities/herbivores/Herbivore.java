package entities.herbivores;

import entities.Animal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Herbivore extends Animal {
    protected Herbivore(Double weight, Integer velocity, Integer limit, Double hunger, String picture) {
        super(weight, velocity, limit, hunger, picture);
    }
}
