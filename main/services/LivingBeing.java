package services;

import constants.Direction;
import entities.Entity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface LivingBeing {

    void eat(Entity entity);
    void breed(LivingBeing animal);
    Direction chooseRoute(ThreadLocalRandom random, List<Direction> directions);
}
