package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Snake extends Predator {
    private static final AtomicLong counter = new AtomicLong(0);
    public Snake(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
    }

    public Snake(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger());
        this.id = animal.getId();
    }

    @Override
    public Snake create(Animal entity) {
        Snake snake = new Snake(entity);
        snake.setId(counter.getAndIncrement());
        return snake;
    }

    @Override
    public Snake copy(Animal entity) {
        return new Snake(entity);
    }
}
