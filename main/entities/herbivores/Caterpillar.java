package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Caterpillar extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);
    public Caterpillar(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Caterpillar(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Caterpillar create(Animal entity) {
        Caterpillar caterpillar = new Caterpillar(entity);
        caterpillar.setId(counter.getAndIncrement());
        return caterpillar;
    }

    @Override
    public Caterpillar copy(Animal entity) {
        return new Caterpillar(entity);
    }
}
