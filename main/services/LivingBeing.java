package services;

import constants.Direction;
import entities.Entity;

public interface LivingBeing {
    void eat(Entity entity);
    void breed(LivingBeing animal);
    void chooseDirection(Direction direction);
}
