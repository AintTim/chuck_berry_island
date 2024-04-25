package entities.predators;

import entities.Animal;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Wolf extends Predator {
    private static final AtomicLong counter = new AtomicLong(0);
    public Wolf(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Wolf(Animal animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
        this.isActionDone = animal.isActionDone();
        this.action = animal.getAction();
    }

    @Override
    public Wolf create(Animal entity) {
        Wolf wolf = new Wolf(entity);
        wolf.setId(counter.getAndIncrement());
        return wolf;
    }

    @Override
    public Wolf copy(Animal entity) {
        return new Wolf(entity);
    }
}
