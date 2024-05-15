package services;

import constants.Direction;
import entities.Animal;
import entities.Entity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface LivingBeing {

    void eat(Entity entity, Integer increase);

    void starve(Integer decrease);

    void breed(Animal animal);

    Direction chooseRoute(ThreadLocalRandom random, List<Direction> directions);
}
