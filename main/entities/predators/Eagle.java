package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
@Getter
public class Eagle extends Predator {
    private static final AtomicLong counter = new AtomicLong(0);

    public Eagle(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Eagle create(Animal entity) {
        Eagle eagle = new Eagle(entity);
        eagle.setId(counter.getAndIncrement());
        return eagle;
    }

    @Override
    public Eagle copy(Animal entity) {
        return new Eagle(entity);
    }
}
