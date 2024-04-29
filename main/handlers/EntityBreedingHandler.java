package handlers;

import configs.EntityConfig;
import constants.Action;
import constants.EntityType;
import entities.Animal;
import entities.Island;

import java.util.concurrent.ThreadLocalRandom;

public class EntityBreedingHandler {
    private final Island island;
    private final EntityConfig config;

    public EntityBreedingHandler(Island island, EntityConfig config) {
        this.island = island;
        this.config = config;
    }

    public Animal getRandomBreedingPartner(Animal animal) {
        var location = island.locateEntity(animal);
        EntityType type = EntityType.ofClass(animal.getClass());
        var partners = island.getFields().get(location).get(type).stream()
                .map(Animal.class::cast)
                .filter(a -> Action.BREED.equals(a.getAction()) && !a.isActionDone())
                .toList();
        return partners.isEmpty()
                ? null
                : partners.get(ThreadLocalRandom.current().nextInt(partners.size()));
    }
}
