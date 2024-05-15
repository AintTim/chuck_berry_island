package handlers;

import configs.EntityConfig;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Island;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EatingHandler {
    private final Island island;
    private final EntityConfig config;
    private static final int PERCENTAGE_POSSIBILITY = 100;
    private static final double REQUIRED_FOOD_RATIO = 0.75;

    public EatingHandler(Island island, EntityConfig config) {
        this.island = island;
        this.config = config;
    }

    public List<Entity> feedAnimal(Animal animal) {
        var preyType = getPrey(animal);
        if (Objects.isNull(preyType)) {
            return Collections.emptyList();
        }
        var location = island.getFields().get(island.locateEntity(animal));
        return preyType.equals(EntityType.GRASS)
                ? getPlants(location, animal)
                : List.of(getRandomPrey(location, preyType));
    }

    private EntityType getPrey(Animal animal) {
        int chance = ThreadLocalRandom.current().nextInt(PERCENTAGE_POSSIBILITY);
        var preys = config.getEatingProbability().getPreys(animal);

        Comparator<Map.Entry<EntityType, List<Entity>>> maxWeight = (type1, type2)
                -> Double.compare(getRepresentative(type1).getWeight(), getRepresentative(type2).getWeight());

        return island.getFields().get(island.locateEntity(animal)).entrySet().stream()
                .filter(entities -> canBeHunted(entities, preys))
                .filter(entities -> canBeEaten(entities, animal, chance))
                .max(maxWeight)
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private List<Entity> getPlants(EnumMap<EntityType, List<Entity>> location, Animal animal) {
        var grass = location.get(EntityType.GRASS);
        double requiredFoodAmount = animal.getHunger() - animal.getSaturation();
        int number = (grass.size() < Math.ceil(requiredFoodAmount * REQUIRED_FOOD_RATIO))
                ? grass.size()
                : (int) Math.ceil(requiredFoodAmount * REQUIRED_FOOD_RATIO);
        return location.get(EntityType.GRASS).subList(0, number);
    }

    private Entity getRandomPrey(EnumMap<EntityType, List<Entity>> location, EntityType preyType) {
        var preys = location.get(preyType).stream().filter(entity -> !entity.getRemovable()).toList();
        return preys.get(ThreadLocalRandom.current().nextInt(preys.size()));
    }

    private boolean canBeHunted(Map.Entry<EntityType, List<Entity>> entities, List<EntityType> preys) {
        if (!preys.contains(entities.getKey())) {
            return false;
        }
        return entities.getValue().stream().anyMatch(entity -> !entity.getRemovable());
    }

    private boolean canBeEaten(Map.Entry<EntityType, List<Entity>> entities, Animal attacker, int chance) {
        return config.getEatingProbability().canBeEaten(attacker, entities.getKey(), chance);
    }

    private Entity getRepresentative(Map.Entry<EntityType, List<Entity>> entity) {
        return (Entity) config.getTemplate(entity.getKey());
    }
}
