package entities.predators;

import entities.Animal;
import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Snake extends Predator {
    private static final AtomicLong counter = new AtomicLong(0);

    public Snake(Entity animal) {
        super(animal.getWeight(), animal.getVelocity(), animal.getLimit(), animal.getHunger(), animal.getPicture());
        this.id = animal.getId();
    }

    @Override
    public Snake create(Animal entity) {
        Snake snake = new Snake(entity);
        snake.setId(counter.getAndIncrement());
        return snake;
    }
}
