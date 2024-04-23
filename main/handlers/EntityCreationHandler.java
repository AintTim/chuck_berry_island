package handlers;

import configs.EntityConfig;
import constants.EntityType;
import entities.Entity;
import services.CreatorService;

import java.util.List;
import java.util.stream.IntStream;

public class EntityCreationHandler {
    private final EntityConfig entityConfig;

    public EntityCreationHandler(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public Entity createEntity(EntityType type) {
        CreatorService<? extends Entity> template = entityConfig.getTemplates().get(type);
        return template.create((Entity) template);
    }

    public List<Entity> createEntities(EntityType type, int number) {
        return IntStream.range(0, number)
                .mapToObj(x -> createEntity(type))
                .toList();
    }
}
