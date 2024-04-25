package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Sheep extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);
    public Sheep(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }
    public Sheep(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Sheep create(Animal entity) {
        Sheep sheep = new Sheep(entity);
        sheep.setId(counter.getAndIncrement());
        return sheep;
    }

    @Override
    public Sheep copy(Animal entity) {
        return new Sheep(entity);
    }
}
