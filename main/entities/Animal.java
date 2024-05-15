package entities;

import constants.Action;
import constants.Direction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import services.CreatorService;
import services.LivingBeing;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class Animal extends Entity implements LivingBeing, CreatorService<Animal, Animal> {
    @Setter
    protected boolean isActionDone;
    @Setter
    protected boolean hasOffspring;
    @Setter
    protected Action action;
    protected int health;
    protected Double saturation;

    protected Animal(Double weight, Integer velocity, Integer limit, Double hunger, String picture) {
        super(weight, velocity, limit, hunger, picture);
        this.action = Action.IDLE;
        this.health = 100;
        this.saturation = hunger;
        this.removable = false;
        this.hasOffspring = false;
    }

    @Override
    public synchronized void eat(Entity entity, Integer increase) {
        saturation = (entity.getWeight() > (hunger - saturation))
                ? hunger
                : saturation + entity.getWeight();
        if (saturation.equals(hunger)) {
            health += increase;
            if (health > 100) {
                health = 100;
            }
        }
    }

    @Override
    public synchronized void starve(Integer decrease) {
        if (!saturation.equals(hunger)) {
            health -= decrease;
            if (health <= 0) {
                removable = true;
            }
        } else {
            saturation -= Math.ceil(saturation / decrease * 100);
            if (saturation < 0) {
                saturation = 0.0;
            }
        }
    }

    @Override
    public synchronized void breed(Animal animal) {
        this.setHasOffspring(true);
        animal.setHasOffspring(true);
    }

    @Override
    public Direction chooseRoute(ThreadLocalRandom random, List<Direction> directions) {
        return directions.get(random.nextInt(directions.size()));
    }

    @Override
    public String toString() {
        return String.format("%d%s (%d:%s)", id, picture, health, removable);
    }
}
