package entities.plants;

import entities.Entity;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class Grass extends Plant {
    private static final AtomicLong counter = new AtomicLong(0);

    public Grass(Entity entity) {
        super(entity.getWeight(), entity.getVelocity(), entity.getLimit(), entity.getHunger(), entity.getPicture());
        this.id = entity.getId();
    }

    @Override
    public Grass create(Plant entity) {
        Grass grass = new Grass(entity);
        grass.setId(counter.getAndIncrement());
        return grass;
    }
}
