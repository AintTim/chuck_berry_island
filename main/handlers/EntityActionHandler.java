package handlers;

import configs.EntityConfig;
import constants.Action;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;
import entities.Island;
import entities.predators.Bear;
import entities.predators.Wolf;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class EntityActionHandler {

    private final Island island;
    private final EntityConfig config;

    public EntityActionHandler(Island island, EntityConfig config) {
        this.island = island;
        this.config = config;
    }

    public void setRandomAction(Animal animal) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Field field = island.locateEntity(animal);
        if (canBreed(animal, field)) {
            if (canEat(animal,field)) {
                animal.setAction(Action.values()[random.nextInt(3)]);
            } else {
                animal.setAction(Action.values()[random.nextInt(1, 3)]);
            }
        } else {
            if (canEat(animal, field)) {
                animal.setAction(Action.values()[random.nextInt(2)]);
            } else {
                animal.setAction(Action.MOVE);
            }
        }
        if (animal instanceof Bear wolf) {
            System.out.printf("%s-%d выбрал следующее действие: %s%n", wolf.getPicture(), wolf.getId(), wolf.getAction());
        }
    }

    private boolean canEat(Animal animal, Field field) {
        return !animal.getSaturation().equals(animal.getHunger()) && !getEdibleNeighbors(field, animal).isEmpty();
    }

    private boolean canBreed(Animal animal, Field field) {
        Predicate<Animal> hasValidStatus =
                a -> Action.IDLE.equals(a.getAction()) || Action.BREED.equals(a.getAction());
        return !animal.isHasOffspring() && !getBreedingNeighbors(field, animal, hasValidStatus).isEmpty();
    }

    private List<Entity> getEdibleNeighbors(Field location, Animal animal) {
        var preys = config.getEatingProbability().getPreys(animal);
        return island.getFields().get(location).entrySet().stream()
                .filter(field -> preys.contains(field.getKey()))
                .flatMap(field -> field.getValue().stream())
                .toList();
    }

    private List<Animal> getBreedingNeighbors(Field field, Animal animal, Predicate<Animal> condition) {
        EntityType type = EntityType.ofClass(animal.getClass());
        return island.getFields().get(field).get(type).stream()
                .map(Animal.class::cast)
                .filter(a -> !a.equals(animal))
                .filter(condition)
                .toList();
    }


}
