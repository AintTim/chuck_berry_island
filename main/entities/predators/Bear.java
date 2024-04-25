package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Setter
@NoArgsConstructor
@Getter
public class Bear extends Predator {
    private static final AtomicLong counter = new AtomicLong(0);

    public Bear(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Bear create(Animal entity) {
        Bear bear = new Bear(entity);
        bear.setId(counter.getAndIncrement());
        return bear;
    }

    @Override
    public Bear copy(Animal entity) {
        return new Bear(entity);
    }
}
