package handlers;

import constants.Action;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;
import entities.Island;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class EntityActionHandler {

    private final Map<Field, EnumMap<EntityType, List<Entity>>> fields;

    public EntityActionHandler(Island island) {
        this.fields = island.getFields();
    }

    public void prepareEntities() {
        for (var field : fields.entrySet()) {
            field.getValue().values().stream().flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .forEach(animal -> setRandomAction(field.getKey(), (Animal) animal));
        }
    }

    public void setRandomAction(Field field, Animal animal) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Predicate<Animal> hasValidStatus = a -> Action.IDLE.equals(a.getAction()) || Action.BREED.equals(a.getAction());

        if (animal.isHasOffspring() || getNeighbors(field, animal, hasValidStatus).isEmpty()) {
            animal.setAction(Action.values()[random.nextInt(2)]);
        } else {
            animal.setAction(Action.values()[random.nextInt(3)]);
        }
    }

    private List<Animal> getNeighbors(Field field, Animal animal, Predicate<Animal> condition) {
        EntityType type = EntityType.ofClass(animal.getClass());
        return fields.get(field).get(type).stream()
                .map(Animal.class::cast)
                .filter(a -> !a.equals(animal))
                .filter(condition)
                .toList();
    }


}
