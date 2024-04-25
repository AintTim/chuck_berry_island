package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Duck extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);
    public Duck(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Duck(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Duck create(Animal entity) {
        Duck duck = new Duck(entity);
        duck.setId(counter.getAndIncrement());
        return duck;
    }

    @Override
    public Duck copy(Animal entity) {
        return new Duck(entity);
    }
}
