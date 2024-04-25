package handlers;

import configs.EntityTemplateConfig;
import constants.Direction;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;
import entities.Island;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class EntityMovementHandler {
    private final Map<Field, EnumMap<EntityType, List<Entity>>> fields;
    private final EntityTemplateConfig templateConfig;

    public EntityMovementHandler(Island island, EntityTemplateConfig templateConfig) {
        this.fields = island.getFields();
        this.templateConfig = templateConfig;
    }

    public Field moveEntity(Animal entity) {
        Field current = locateEntity(entity);
        System.out.printf("%s:%s moved from (%s)%n", entity,entity.hashCode(), current);
        for (int move = 0; move < entity.getVelocity(); move++) {
            List<Direction> possibleDirections = definePossibleDirections(current, entity);
            current = move(current, entity, possibleDirections);
        }
        System.out.printf("%s:%s moved to (%s)%n", entity,entity.hashCode(), current);
        System.out.println("_____________________________________");
        return current;
    }

    private Field move(Field start, Animal animal, List<Direction> directions) {
        return directions.isEmpty()
                ? start
                : getFieldByDirection(start, animal.chooseRoute(ThreadLocalRandom.current(), directions));
    }

    private Field locateEntity(Entity entity) {
        for (var field : fields.entrySet()) {
            var fieldMap = field.getValue();
            var entities = fieldMap.get(EntityType.ofClass(entity.getClass()));
            if (entities.contains(entity)) {
                return field.getKey();
            }
        }
        throw new IllegalArgumentException("Искомая сущность отсутствует на острове");
    }

    private List<Direction> definePossibleDirections(Field location, Entity entity) {
        Predicate<Field> isWithinBoundaries = fields::containsKey;
        Predicate<Field> isWithinNumberLimitation = field -> {
            EntityType type = EntityType.ofClass(entity.getClass());
            return fields.get(field).get(type).size() < entity.getLimit();
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
