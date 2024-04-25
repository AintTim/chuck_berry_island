package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Horse extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);
    public Horse(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Horse(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Horse create(Animal entity) {
        Horse horse = new Horse(entity);
        horse.setId(counter.getAndIncrement());
        return horse;
    }

    @Override
    public Horse copy(Animal entity) {
        return new Horse(entity);
    }
}
