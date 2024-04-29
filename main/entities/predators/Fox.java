package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Fox extends Predator {
    private static final AtomicLong counter = new AtomicLong(0);

    public Fox(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Fox create(Animal entity) {
        Fox fox = new Fox(entity);
        fox.setId(counter.getAndIncrement());
        return fox;
    }
}
