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
    protected Action action;
    protected int health;
    protected Animal(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
        this.action = Action.IDLE;
        this.health = 100;
        this.isRemovable = false;
    }

    protected Animal(Double weight, Integer velocity, Integer limit, Double hunger, boolean isActionDone, Action action, int health) {
        super(weight, velocity, limit, hunger);
        this.isActionDone = isActionDone;
        this.action = action;
        this.health = health;
    }

    @Override
    public void eat(Entity entity) {

    }

    @Override
    public void breed(LivingBeing animal) {

    }

    @Override
    public void move() {
        this.isActionDone = true;
        this.action = Action.IDLE;
    }

    @Override
    public Direction chooseRoute(ThreadLocalRandom random, List<Direction> directions) {
        return directions.get(random.nextInt(directions.size()));
    }
}
