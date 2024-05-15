package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Caterpillar extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);

    public Caterpillar(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Caterpillar create(Animal entity) {
        Caterpillar caterpillar = new Caterpillar(entity);
        caterpillar.setId(counter.getAndIncrement());
        return caterpillar;
    }
}
