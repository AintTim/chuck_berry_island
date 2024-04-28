package handlers;

import constants.Direction;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;
import entities.Island;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class EntityMovementHandler {
    private final Island island;

    public EntityMovementHandler(Island island) {
        this.island = island;
    }

    public Field moveEntity(Animal entity) {
        Field current = island.locateEntity(entity);
        for (int move = 0; move < entity.getVelocity(); move++) {
            List<Direction> possibleDirections = definePossibleDirections(current, entity);
            current = move(current, entity, possibleDirections);
        }
        return current;
    }

    private Field move(Field start, Animal animal, List<Direction> directions) {
        return directions.isEmpty()
                ? start
                : getFieldByDirection(start, animal.chooseRoute(ThreadLocalRandom.current(), directions));
    }

    private List<Direction> definePossibleDirections(Field location, Entity entity) {
        Predicate<Field> isWithinBoundaries = island.getFields()::containsKey;
        Predicate<Field> isWithinNumberLimitation = field -> {
            EntityType type = EntityType.ofClass(entity.getClass());
            return island.getFields().get(field).get(type).stream().filter(animal -> !animal.getRemovable()).count() < entity.getLimit();
        };
        return Arrays.stream(Direction.values())
                .filter(direction -> directionValidation(location, direction, isWithinBoundaries))
                .filter(direction -> directionValidation(location, direction, isWithinNumberLimitation))
                .toList();
    }

    private Field getFieldByDirection(Field current, Direction direction) {
        return switch (direction) {
            case UP -> new Field(current.getX(), current.getY() - 1);
            case RIGHT -> new Field(current.getX() + 1, current.getY());
            case DOWN -> new Field(current.getX(), current.getY() + 1);
            case LEFT -> new Field(current.getX() - 1, current.getY());
        };
    }

    private boolean directionValidation(Field location, Direction direction, Predicate<Field> validation) {
        return switch (direction) {
            case UP -> validation.test(new Field(location.getX(), location.getY() - 1));
            case RIGHT -> validation.test(new Field(location.getX() + 1, location.getY()));
            case DOWN -> validation.test(new Field(location.getX(), location.getY() + 1));
            case LEFT -> validation.test(new Field(location.getX() - 1, location.getY()));
        };
    }
}
