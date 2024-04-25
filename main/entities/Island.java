package entities;

import configs.IslandConfig;
import constants.Action;
import constants.EntityType;
import entities.plants.Plant;
import lombok.Getter;
import services.ManageEntityService;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

@Getter
public class Island implements ManageEntityService {
    private final Map<Field, EnumMap<EntityType, List<Entity>>> fields = new HashMap<>();
    private final IslandConfig config;

    public Island(IslandConfig config) {
        initFields(config, fields);
        this.config = config;
    }

    private static void initFields(IslandConfig config, Map<Field, EnumMap<EntityType, List<Entity>>> fields) {
        for (int x = 0; x < config.getWidth(); x++) {
            for (int y = 0; y < config.getHeight(); y++) {
                fields.put(new Field(x, y), new EnumMap<>(EntityType.class));
            }
        }
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
        for (var field : fields.entrySet()) {
            field.getValue().values().stream()
                    .flatMap(Collection::stream)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast)
                    .filter(animal -> Action.MOVE.equals(animal.getAction()))
                    .forEach(animal -> moveAnimal(field.getKey(), animal, relocate));
        }
    }

    private void moveAnimal(Field start, Animal animal, Function<Animal, Field> relocate) {
        Field destination = relocate.apply(animal);
        animal.setActionDone(true);
        animal.setAction(Action.IDLE);
        if (!start.equals(destination)) {
            var copy = animal.copy(animal);
            animal.setIsRemovable(true);
            fields.get(destination).get(EntityType.ofClass(animal.getClass())).add(copy);
        }
        System.out.printf("%s-%d переместился из %s в %s%n", animal.getClass().getSimpleName(), animal.getId(), start, destination);
    }

    private void fillWithRandomNumberOfPlants(Entity plant, IntFunction<List<Entity>> createPlant, EnumMap<EntityType, List<Entity>> map) {
        if (Objects.isNull(map.get(EntityType.GRASS))) {
            map.put(EntityType.GRASS, new ArrayList<>(createPlant.apply(plant.getLimit())));
        } else {
            List<Entity> plants = map.get(EntityType.GRASS);
            int difference = plant.getLimit() - plants.size();
            if (difference > 0) {
                plants.addAll(createPlant.apply(difference));
            }
        }
    }
}
