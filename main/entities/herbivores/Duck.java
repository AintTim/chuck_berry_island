package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Duck extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);

    public Duck(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Duck create(Animal entity) {
        Duck duck = new Duck(entity);
        duck.setId(counter.getAndIncrement());
        return duck;
    }
}
