package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Deer extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);

    public Deer(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Deer create(Animal entity) {
        Deer deer = new Deer(entity);
        deer.setId(counter.getAndIncrement());
        return deer;
    }
}
