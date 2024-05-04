package entities;

import configs.EntityConfig;
import configs.IslandConfig;
import constants.Action;
import constants.EntityType;
import handlers.StatisticsHandler;
import lombok.Getter;
import services.ManageEntityService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;
import java.util.stream.Stream;

@Getter
public class Island implements ManageEntityService {
    private final ConcurrentMap<Field, EnumMap<EntityType, List<Entity>>> fields = new ConcurrentHashMap<>();
    private final IslandConfig config;
    private final EntityConfig entityConfig;
    private final StatisticsHandler statistics;

    public Island(IslandConfig island, EntityConfig entity, StatisticsHandler statistics) {
        initFields(island, fields);
        this.config = island;
        this.entityConfig = entity;
        this.statistics = statistics;
    }

    //убрать мертвых животных - Статистика по умершим животным
    @Override
    public void resetEntities(Predicate<Entity> condition) {
        fields.values().stream().flatMap(map -> map.values().stream())
                .forEach(list -> list.removeIf(condition));
    }

    //восполнить растения
    @Override
    public synchronized void refillPlants(IntFunction<List<Entity>> createPlant) {

        fields.values().forEach(map -> fillWithRandomNumberOfPlants(createPlant, map));
        System.out.println("Plants are refilled");
    }

//    @Override
//    public void moveAnimals(Function<Animal, Field> relocate) {
//        Predicate<Animal> isMoving = animal -> Action.MOVE.equals(animal.getAction()) && !animal.getRemovable();
//
//        for (var field : fields.entrySet()) {
//            field.getValue().values().stream()
//                    .flatMap(Collection::stream)
//                    .filter(Animal.class::isInstance)
//                    .map(Animal.class::cast)
//                    .filter(isMoving)
//                    .forEach(animal -> moveAnimal(field.getKey(), animal, relocate));
//        }
//    }

    @Override
    public void moveAnimals(EntityType type, Function<Animal, Field> relocate) {
        Predicate<Animal> isMoving = animal -> Action.MOVE.equals(animal.getAction()) && !animal.getRemovable();
        for (var field : fields.entrySet()) {
            getEntitiesOfType(field.getValue().entrySet().stream(), type, Animal.class)
                    .filter(isMoving)
                    .forEach(animal -> moveAnimal(field.getKey(), animal, relocate));
        }
    }

//    @Override
//    public void feedAnimals(Function<Animal, List<Entity>> eat) {
//        Predicate<Animal> isEating = animal -> Action.EAT.equals(animal.getAction());
//
//        for (var field : fields.entrySet()) {
//            field.getValue().values().stream()
//                    .flatMap(Collection::stream)
//                    .filter(Animal.class::isInstance)
//                    .map(Animal.class::cast)
//                    .filter(isEating)
//                    .forEach(animal -> eatEntity(field.getKey(), animal, eat));
//        }
//    }

    @Override
    public void feedAnimals(EntityType type, Function<Animal, List<Entity>> eat) {
        Predicate<Animal> isEating = animal -> Action.EAT.equals(animal.getAction()) && !animal.getRemovable();
        for (var field : fields.entrySet()) {
            getEntitiesOfType(field.getValue().entrySet().stream(), type, Animal.class)
                    .filter(isEating)
                    .forEach(animal -> eatEntity(field.getKey(), animal, eat));
        }
    }

//    @Override
//    public void prepareAnimals(Consumer<Animal> prepare) {
//        for (var field : fields.entrySet()) {
//            field.getValue().values().stream().flatMap(Collection::stream)
//                    .filter(Animal.class::isInstance)
//                    .map(Animal.class::cast)
//                    .forEach(prepare);
//        }
//    }

    @Override
    public synchronized void prepareAnimals(EntityType type, Consumer<Animal> prepare) {
        for (var field : fields.entrySet()) {
            field.getValue().entrySet().stream()
                    .filter(map -> map.getKey().equals(type))
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .forEach(prepare);
        }
    }

    @Override
    public void breedAnimals(EntityType type, UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring) {
        Predicate<Animal> isBreeding = animal -> Action.BREED.equals(animal.getAction()) && !animal.getRemovable();
        for (var field : fields.entrySet()) {
            getEntitiesOfType(field.getValue().entrySet().stream(), type, Animal.class)
                    .filter(isBreeding)
                    .forEach(animal -> breedAnimal(field.getKey(), animal, breed, createOffspring));
        }
    }

//    @Override
//    public void breedAnimals(UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring) {
//        Predicate<Animal> isBreeding = animal -> Action.BREED.equals(animal.getAction());
//
//        for (var field : fields.entrySet()) {
//            field.getValue().values().stream()
//                    .flatMap(Collection::stream)
//                    .filter(Animal.class::isInstance)
//                    .map(Animal.class::cast)
//                    .filter(isBreeding)
//                    .forEach(animal -> breedAnimal(field.getKey(), animal, breed, createOffspring));
//        }
//    }

    public synchronized void countEntities() {
        for (var field : fields.entrySet()) {
            field.getValue().forEach((type, list) ->
                    list.forEach(entity -> statistics.getTotal().merge(type, 1, Integer::sum)));
        }
        System.out.println("Entities are calculated!");
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

    private static void initFields(IslandConfig config, Map<Field, EnumMap<EntityType, List<Entity>>> fields) {
        for (int x = 0; x < config.getWidth(); x++) {
            for (int y = 0; y < config.getHeight(); y++) {
                fields.put(new Field(x, y), new EnumMap<>(EntityType.class));
            }
        }
    }

    private void breedAnimal(Field location, Animal animal, UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring) {
        Animal partner = breed.apply(animal);
        if (Objects.nonNull(partner)) {
            updateAnimalStatus(partner, true);
            EntityType type = EntityType.ofClass(animal.getClass());
            List<Entity> animals = fields.get(location).get(type);
            if (animals.size() + 1 <= animal.getLimit()) {
                var offspring = createOffspring.apply(type);
                animal.breed(partner);
                var updatedEntities = new ArrayList<>(animals);
                updatedEntities.add(offspring);
                fields.get(location).put(type, updatedEntities);
                statistics.getBorn().merge(type, 1, Integer::sum);
            }
        }
        updateAnimalStatus(animal, true);
    }

    private void moveAnimal(Field start, Animal animal, Function<Animal, Field> relocate) {
        Field destination = relocate.apply(animal);
        updateAnimalStatus(animal, true);
        if (!start.equals(destination)) {
            removeEntity(start, animal);
            EntityType type = EntityType.ofClass(animal.getClass());
            fields.get(destination).get(type).add(animal);
            statistics.getRelocated().merge(type, 1, Integer::sum);
        }
    }

    private void eatEntity(Field location, Animal attacker, Function<Animal, List<Entity>> eat) {
        List<Entity> prey = eat.apply(attacker);
        if (prey.isEmpty()) {
            updateAnimalStatus(attacker, true);
            return;
        }
        if (prey.size() == 1) {
            eatSingleEntity(location, attacker, prey.get(0));
        } else {
            eatMultipleEntities(location, attacker, prey);
        }
    }

    private void eatMultipleEntities(Field location, Animal attacker, List<Entity> preys) {
        List<Entity> eatenGrass = new ArrayList<>();
        preys.forEach(grass -> {
            if (removeEntity(location, grass)) {
                eatenGrass.add(grass);
                attacker.eat(grass, entityConfig.getHealthRecoverWithFullSaturation());
                statistics.updatePlantStat(attacker, 1);
            }
        });
        updateAnimalStatus(attacker, !eatenGrass.isEmpty());
    }

    private void eatSingleEntity(Field location, Animal attacker, Entity prey) {
        if (removeEntity(location, prey)) {
            updateAnimalStatus(attacker, false);
            attacker.eat(prey, entityConfig.getHealthRecoverWithFullSaturation());
            if (EntityType.ofClass(prey.getClass()).equals(EntityType.GRASS)) {
                statistics.updatePlantStat(attacker, 1);
            } else {
                statistics.getAnimalsEaten().merge(EntityType.ofClass(prey.getClass()), 1, Integer::sum);
            }
        } else {
            updateAnimalStatus(attacker, true);
        }
    }

    private synchronized void fillWithRandomNumberOfPlants(IntFunction<List<Entity>> createPlant, EnumMap<EntityType, List<Entity>> map) {
        List<Entity> plants = map.get(EntityType.GRASS);
        Entity grass = (Entity) entityConfig.getTemplate(EntityType.GRASS);
        int difference = grass.getLimit() - plants.size();
        if (difference > 0) {
            var newPlants = createPlant.apply(difference);
            plants.addAll(newPlants);
            statistics.getPlantsGrown().addAndGet(newPlants.size());
        }
    }

    private synchronized boolean removeEntity(Field location, Entity entity) {
        EntityType type = EntityType.ofClass(entity.getClass());
        var updatedEntities = new ArrayList<>(fields.get(location).get(type));
        var isRemoved = updatedEntities.remove(entity);
        fields.get(location).put(type, updatedEntities);
        return isRemoved;
    }

    private void updateAnimalStatus(Animal animal, boolean isHungry) {
        animal.setActionDone(true);
        animal.setAction(Action.IDLE);
        if (isHungry) {
            animal.starve(entityConfig.getStarvingHealthReduction());
        }
    }

    private <T extends Entity> Stream<T> getEntitiesOfType(Stream<Map.Entry<EntityType,List<Entity>>> stream, EntityType type, Class<T> clazz) {
        return stream
                .filter(map -> map.getKey().equals(type))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .map(clazz::cast);
    }
}
