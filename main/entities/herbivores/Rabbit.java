package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Rabbit extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);

    public Rabbit(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Rabbit create(Animal entity) {
        Rabbit rabbit = new Rabbit(entity);
        rabbit.setId(counter.getAndIncrement());
        return rabbit;
    }
}
