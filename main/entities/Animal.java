package entities;

import constants.Action;
import constants.Direction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import services.LivingBeing;

@NoArgsConstructor
@Getter
public abstract class Animal extends Entity implements LivingBeing {

    private boolean isActionDone;
    private Action action;
    private int health;
    protected Animal(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
        this.action = Action.IDLE;
        this.health = 100;
    }

    @Override
    public void eat(Entity entity) {

    }

    @Override
    public void breed(LivingBeing animal) {

    }

    @Override
    public void chooseDirection(Direction direction) {

    }
}
