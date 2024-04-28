package entities;

import configs.EntityConfig;
import configs.IslandConfig;
import constants.Action;
import constants.EntityType;
import entities.herbivores.Deer;
import entities.plants.Plant;
import entities.predators.Wolf;
import lombok.Getter;
import services.ManageEntityService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Getter
public class Island implements ManageEntityService {
    private final ConcurrentMap<Field, EnumMap<EntityType, List<Entity>>> fields = new ConcurrentHashMap<>();
    private final IslandConfig config;
    private final EntityConfig entityConfig;

    public Island(IslandConfig island, EntityConfig entity) {
        initFields(island, fields);
        this.config = island;
        this.entityConfig = entity;
    }

    private static void initFields(IslandConfig config, Map<Field, EnumMap<EntityType, List<Entity>>> fields) {
        for (int x = 0; x < config.getWidth(); x++) {
            for (int y = 0; y < config.getHeight(); y++) {
                fields.put(new Field(x, y), new EnumMap<>(EntityType.class));
            }
        }
    }

    public Field locateEntity(Entity entity) {
        for (var field : fields.entrySet()) {
            var fieldMap = field.getValue();
            var entities = fieldMap.get(EntityType.ofClass(entity.getClass()));
            if (entities.contains(entity)) {
                return field.getKey();
            }
        }
        throw new IllegalArgumentException("Искомая сущность отсутствует на острове");
    }

    //убрать мертвых животных - Статистика по умершим животным
    @Override
    public void resetEntities(Predicate<Entity> condition) {
        fields.values().stream().flatMap(map -> map.values().stream())
                .forEach(list -> list.removeIf(condition));
    }

    //восполнить растения
    @Override
    public void refillPlants(Entity plant, IntFunction<List<Entity>> createPlant) {
        if (!(plant instanceof Plant)) {
            throw new IllegalArgumentException(String.format("Передаваемая сущность должная являться Plant (%s)", plant.getClass().getSimpleName()));
        }
        fields.values().forEach(map -> fillWithRandomNumberOfPlants(plant, createPlant, map));
    }

    @Override
    public void moveAnimals(Function<Animal, Field> relocate) {
        Predicate<Animal> isMoving = animal -> Action.MOVE.equals(animal.getAction()) && !animal.getRemovable();

        for (var field : fields.entrySet()) {
            field.getValue().values().stream()
                    .flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .filter(isMoving)
                    .forEach(animal -> moveAnimal(field.getKey(), animal, relocate));
        }
    }

    @Override
    public void feedAnimals(Function<Animal, List<Entity>> eat) {
        Predicate<Animal> isEating = animal -> Action.EAT.equals(animal.getAction()) && !animal.getRemovable();

        for (var field : fields.entrySet()) {
            field.getValue().values().stream()
                    .flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .filter(isEating)
                    .forEach(animal -> eatEntity(field.getKey(), animal, eat));
        }
    }

    private void moveAnimal(Field start, Animal animal, Function<Animal, Field> relocate) {
        Field destination = relocate.apply(animal);
        animal.setActionDone(true);
        animal.setAction(Action.IDLE);
        animal.starve(entityConfig.getStarvingHealthReduction());
        if (!start.equals(destination)) {
            removeEntity(start, animal);
            EntityType type = EntityType.ofClass(animal.getClass());
            fields.get(destination).get(type).add(animal);
        }
        //TODO: Статистика перемещенных животных
//        if (animal instanceof Wolf wolf) {
//            System.out.printf("%s-%d переместился из %s в %s%n", wolf.getClass().getSimpleName(), wolf.getId(), start, destination);
//        }
    }

    private void eatEntity(Field location, Animal attacker, Function<Animal, List<Entity>> eat) {
        List<Entity> prey = eat.apply(attacker);
        attacker.setActionDone(true);
        attacker.setAction(Action.IDLE);
        if (prey.isEmpty()) {
            attacker.starve(entityConfig.getStarvingHealthReduction());
        } else if (prey.size() == 1) {
            if (attacker instanceof Wolf wolf) {
                System.out.printf("%s-%d съел %s-%d с текущим уровнем сытости %s и здоровьем %d%n", wolf.getClass(), wolf.getId(), prey.get(0).getClass(), prey.get(0).getId(), wolf.getSaturation(), wolf.getHealth());
            }
            attacker.eat(prey.get(0), entityConfig.getHealthRecoverWithFullSaturation());
            removeEntity(location, prey.get(0));
            if (attacker instanceof Wolf wolf) {
                System.out.printf("%s-%d восстановил уровень сытости до %s с здоровьем %d%n", wolf.getClass(), wolf.getId(), wolf.getSaturation(), wolf.getHealth());
            }
        } else {
            if (attacker instanceof Deer deer) {
                System.out.printf("%s-%d съел %d кг травы с текущим уровнем сытости %s и здоровьем %d%n", deer.getClass(), deer.getId(), prey.size(), deer.getSaturation(), deer.getHealth());
            }
            prey.forEach(grass -> attacker.eat(grass, entityConfig.getHealthRecoverWithFullSaturation()));
            removeEntities(location, prey);
            if (attacker instanceof Deer deer) {
                System.out.printf("%s-%d восстановил уровень сытости до %s с здоровьем %d%n", deer.getClass(), deer.getId(), deer.getSaturation(), deer.getHealth());
            }
        }
    }

    private void removeEntities(Field location, List<Entity> entities) {
        EntityType type = EntityType.ofClass(entities.get(0).getClass());
        var updatedEntities = new ArrayList<>(fields.get(location).get(type));
        updatedEntities.removeAll(entities);
        fields.get(location).put(type, updatedEntities);
    }

    private void removeEntity(Field location, Entity entity) {
        EntityType type = EntityType.ofClass(entity.getClass());
        var updatedEntities = new ArrayList<>(fields.get(location).get(type));
        updatedEntities.remove(entity);
        fields.get(location).put(type, updatedEntities);
    }

    private void fillWithRandomNumberOfPlants(Entity plant, IntFunction<List<Entity>> createPlant, EnumMap<EntityType, List<Entity>> map) {
        List<Entity> plants = map.get(EntityType.GRASS);
        int difference = plant.getLimit() - plants.size();
        if (difference > 0) {
            plants.addAll(createPlant.apply(difference));
        }
        //TODO: Статистика созданной травы
    }
}
