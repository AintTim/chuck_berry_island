package entities;

import constants.Direction;
import services.LivingBeing;

public abstract class Animal extends Entity implements LivingBeing {

    private int health;
    protected Animal(Double weight, Integer velocity, Integer limit, Double hunger) {
        super(weight, velocity, limit, hunger);
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
