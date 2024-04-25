package entities.herbivores;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Mouse extends Herbivore {
    private static final AtomicLong counter = new AtomicLong(0);
    public Mouse(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Mouse(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Mouse create(Animal entity) {
        Mouse mouse = new Mouse(entity);
        mouse.setId(counter.getAndIncrement());
        return mouse;
    }

    @Override
    public Mouse copy(Animal entity) {
        return new Mouse(entity);
    }
}
