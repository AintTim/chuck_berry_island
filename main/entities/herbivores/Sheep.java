package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Sheep extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);
    public Sheep(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Sheep create(Animal entity) {
        Sheep sheep = new Sheep(entity);
        sheep.setId(counter.getAndIncrement());
        return sheep;
    }
}
