package handlers;

import configs.EntityTemplateConfig;
import constants.EntityType;
import entities.Entity;

import java.lang.reflect.InvocationTargetException;

public class EntityHandler {
    private EntityTemplateConfig entityConfig;

    public EntityHandler(EntityTemplateConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T createEntity(EntityType type) {
        Class<? extends Entity> clazz = type.getObjectClass();
        Entity template = entityConfig.getTemplates().get(type);
        try {
            return (T) clazz.getDeclaredConstructor(Entity.class).newInstance(template);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
