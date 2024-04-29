package entities;

import configs.EntityConfig;
import configs.IslandConfig;
import constants.Action;
import constants.EntityType;
import entities.herbivores.Deer;
import entities.predators.Wolf;
import lombok.Getter;
import services.ManageEntityService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;

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

    //убрать мертвых животных - Статистика по умершим животным
    @Override
    public void resetEntities(Predicate<Entity> condition) {
        fields.values().stream().flatMap(map -> map.values().stream())
                .forEach(list -> list.removeIf(condition));
    }

    //восполнить растения
    @Override
    public void refillPlants(IntFunction<List<Entity>> createPlant) {

        fields.values().forEach(map -> fillWithRandomNumberOfPlants(createPlant, map));
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
        Predicate<Animal> isEating = animal -> Action.EAT.equals(animal.getAction());

        for (var field : fields.entrySet()) {
            field.getValue().values().stream()
                    .flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .filter(isEating)
                    .forEach(animal -> eatEntity(field.getKey(), animal, eat));
        }
    }

    @Override
    public void prepareAnimals(Consumer<Animal> prepare) {
        for (var field : fields.entrySet()) {
            field.getValue().values().stream().flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .forEach(prepare);
        }
    }

    @Override
    public void breedAnimals(UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring) {
        Predicate<Animal> isBreeding = animal -> Action.BREED.equals(animal.getAction());

        for (var field : fields.entrySet()) {
            field.getValue().values().stream()
                    .flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .filter(isBreeding)
                    .forEach(animal -> breedAnimal(field.getKey(), animal, breed, createOffspring));
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

    private static void initFields(IslandConfig config, Map<Field, EnumMap<EntityType, List<Entity>>> fields) {
        for (int x = 0; x < config.getWidth(); x++) {
            for (int y = 0; y < config.getHeight(); y++) {
                fields.put(new Field(x, y), new EnumMap<>(EntityType.class));
            }
        }
    }

    private void breedAnimal(Field location, Animal animal, UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring) {
        Animal partner = breed.apply(animal);
        updateAnimalStatus(animal, true);
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
                System.out.printf("Пара: %s-%s и %s-%s\t Потомство: %s-%s%n",
                        animal.getClass().getSimpleName(), animal.getId(),
                        partner.getClass().getSimpleName(), partner.getId(),
                        offspring.getClass().getSimpleName(), offspring.getId());
                //TODO: Статистика новорожденных животных
            } else {
                System.out.printf("Пара: %s-%s и %s-%s\t не могут дать потомства из-за перенаселенности клетки %d%n",
                        animal.getClass().getSimpleName(), animal.getId(),
                        partner.getClass().getSimpleName(), partner.getId(),
                        animals.size());
            }
        } else {
            System.out.printf("%s-%s не нашёл пару на клетке %s%n",
                    animal.getClass().getSimpleName(), animal.getId(), location);
        }
    }

    private void moveAnimal(Field start, Animal animal, Function<Animal, Field> relocate) {
        Field destination = relocate.apply(animal);
        updateAnimalStatus(animal, true);
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
        if (prey.isEmpty()) {
            updateAnimalStatus(attacker, true);
        } else if (prey.size() == 1) {
            if (attacker instanceof Wolf wolf) {
                System.out.printf("%s-%d съел %s-%d с текущим уровнем сытости %s и здоровьем %d%n", wolf.getClass().getSimpleName(), wolf.getId(), prey.get(0).getClass().getSimpleName(), prey.get(0).getId(), wolf.getSaturation(), wolf.getHealth());
            }
            updateAnimalStatus(attacker, false);
            attacker.eat(prey.get(0), entityConfig.getHealthRecoverWithFullSaturation());
            removeEntity(location, prey.get(0));
            if (attacker instanceof Wolf wolf) {
                System.out.printf("%s-%d восстановил уровень сытости до %s с здоровьем %d%n", wolf.getClass().getSimpleName(), wolf.getId(), wolf.getSaturation(), wolf.getHealth());
            }
        } else {
            if (attacker instanceof Deer deer) {
                System.out.printf("%s-%d съел %d кг травы с текущим уровнем сытости %s и здоровьем %d%n", deer.getClass().getSimpleName(), deer.getId(), prey.size(), deer.getSaturation(), deer.getHealth());
            }
            updateAnimalStatus(attacker, false);
            prey.forEach(grass -> attacker.eat(grass, entityConfig.getHealthRecoverWithFullSaturation()));
            removeEntities(location, prey);
            if (attacker instanceof Deer deer) {
                System.out.printf("%s-%d восстановил уровень сытости до %s с здоровьем %d%n", deer.getClass().getSimpleName(), deer.getId(), deer.getSaturation(), deer.getHealth());
            }
        }
    }

    private void fillWithRandomNumberOfPlants(IntFunction<List<Entity>> createPlant, EnumMap<EntityType, List<Entity>> map) {
        List<Entity> plants = map.get(EntityType.GRASS);
        Entity grass = (Entity) entityConfig.getTemplate(EntityType.GRASS);
        int difference = grass.getLimit() - plants.size();
        if (difference > 0) {
            plants.addAll(createPlant.apply(difference));
        }
        //TODO: Статистика созданной травы
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

    private void updateAnimalStatus(Animal animal, boolean isHungry) {
        animal.setActionDone(true);
        animal.setAction(Action.IDLE);
        if (isHungry) {
            animal.starve(entityConfig.getStarvingHealthReduction());
        }
    }
}
