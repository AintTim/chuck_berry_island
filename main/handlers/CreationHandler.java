package handlers;

import configs.EntityConfig;
import constants.EntityType;
import entities.Entity;
import entities.Island;
import services.CreatorService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class CreationHandler {
    private final EntityConfig entityConfig;

    public CreationHandler(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public Entity createEntity(EntityType type) {
        CreatorService<Entity, Entity> template = entityConfig.getTemplate(type);
        return template.create((Entity) template);
    }

    public List<Entity> createEntities(EntityType type, int number) {
        return IntStream.range(0, number)
                .mapToObj(x -> createEntity(type))
                .toList();
    }

    public void fillIslandWithRandomEntities(Island island) {
        for (var field : island.getFields().entrySet()) {
            entityConfig.getTemplates().forEach((type, template) -> {
                int number = ThreadLocalRandom.current().nextInt(((Entity)template).getLimit());
                field.getValue().put(type, new ArrayList<>(createEntities(type, number)));
            });
        }
    }
}
