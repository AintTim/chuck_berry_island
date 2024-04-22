package handlers;

import configs.EntityTemplateConfig;
import constants.EntityType;
import entities.Entity;
import services.CreatorService;

public class EntityHandler {
    private final EntityTemplateConfig entityConfig;

    public EntityHandler(EntityTemplateConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public Entity createEntity(EntityType type) {
        CreatorService<? extends Entity> template = entityConfig.getTemplates().get(type);
        return template.create((Entity) template);
    }
}
