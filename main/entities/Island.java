package entities;

import configs.IslandConfig;
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

    //убрать мертвых животных
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
    public void moveAnimal(Field start, Entity entity, Function<Animal, Field> relocate) {
        if (entity instanceof Animal animal) {
            Field destination = relocate.apply(animal);
            if (!start.equals(destination)) {
                getEntity(start, animal).setIsRemovable(true);
                animal.move();
                fields.get(destination).get(EntityType.ofClass(animal.getClass())).add(animal.copy(animal));
            }
        }
    }

    private Entity getEntity(Field location, Entity entity) {
        EntityType type = EntityType.ofClass(entity.getClass());
        return fields.get(location).get(type).stream()
                .filter(entity::equals)
                .findFirst().orElseThrow(() -> new NoSuchElementException("Искомая сущность не существует"));
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

    @Override
    public String toString() {
        return super.toString();
    }
}
