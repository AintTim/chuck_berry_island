package configs;

import constants.EntityType;
import entities.Entity;
import lombok.Getter;
import services.CreatorService;

import java.util.Map;
import java.util.Properties;

@Getter
public class EntityConfig {
    private final EatingProbabilityConfig eatingProbability;
    private final EntityTemplateConfig entityTemplate;
    private final int starvingHealthReduction;
    private final int healthRecoverWithFullSaturation;

    public EntityConfig(EatingProbabilityConfig eatingProbability, EntityTemplateConfig entityTemplate, Properties properties) {
        this.eatingProbability = eatingProbability;
        this.entityTemplate = entityTemplate;
        this.starvingHealthReduction = Integer.parseInt(properties.getProperty("animal.health_reduction"));
        this.healthRecoverWithFullSaturation = Integer.parseInt(properties.getProperty("animal.health_recover"));
    }

    public Map<EntityType, CreatorService<Entity, Entity>> getTemplates() {
        return entityTemplate.getTemplates();
    }

    public CreatorService<Entity, Entity> getTemplate(EntityType type) {
        return entityTemplate.getTemplates().get(type);
    }
}
