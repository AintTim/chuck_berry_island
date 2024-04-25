package configs;

import constants.EntityType;
import entities.Entity;
import services.CreatorService;

import java.util.Map;

public class EntityConfig {
    private final EatingProbabilityConfig eatingProbability;
    private final EntityTemplateConfig entityTemplate;

    public EntityConfig(EatingProbabilityConfig eatingProbability, EntityTemplateConfig entityTemplate) {
        this.eatingProbability = eatingProbability;
        this.entityTemplate = entityTemplate;
    }

    public Map<EntityType, CreatorService<Entity, Entity>> getTemplates() {
        return entityTemplate.getTemplates();
    }

    public Entity getTemplate(EntityType type) {
        return (Entity) entityTemplate.getTemplates().get(type);
    }

    public Map<EntityType, Map<EntityType, Integer>> getEatingProbability() {
        return eatingProbability.getProbabilities();
    }
}
