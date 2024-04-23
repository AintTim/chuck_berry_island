package entities;

import configs.IslandConfig;
import constants.EntityType;
import entities.plants.Plant;
import lombok.Getter;
import services.ManageEntityService;

import java.util.*;
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
    public void removeEntities(Predicate<Entity> condition) {
        fields.values().stream()
                .flatMap(map -> map.values().stream())
                .forEach(list -> list.removeIf(condition));
    }

    //восполнить растения
    @Override
    public void refillPlants(Entity plant, IntFunction<List<Entity>> createPlant) {
        if (!(plant instanceof Plant)) {
            throw new IllegalArgumentException(String.format("Передаваемая сущность должная являться Plant (%s)", plant.getClass().getSimpleName()));
        }
        fields.values().forEach(map -> {
            if (Objects.isNull(map.get(EntityType.GRASS))) {
                map.put(EntityType.GRASS, new ArrayList<>(createPlant.apply(plant.getLimit())));
            } else {
                List<Entity> plants = map.get(EntityType.GRASS);
                int difference = plant.getLimit() - plants.size();
                if (difference > 0) {
                    plants.addAll(createPlant.apply(difference));
                }
            }
        });
    }
}
