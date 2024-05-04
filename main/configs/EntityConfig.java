package configs;

import constants.EntityType;
import entities.Entity;
import lombok.Getter;
import services.CreatorService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Getter
public class EntityConfig {
    private final EatingProbabilityConfig eatingProbability;
    private final EntityTemplateConfig entityTemplate;
    private final int starvingHealthReduction;
    private final int healthRecoverWithFullSaturation;
    private final int defaultHealth;

    public EntityConfig(EatingProbabilityConfig eatingProbability, EntityTemplateConfig entityTemplate, Properties properties) {
        this.eatingProbability = eatingProbability;
        this.entityTemplate = entityTemplate;
        this.starvingHealthReduction = Integer.parseInt(properties.getProperty("animal.health_reduction"));
        this.healthRecoverWithFullSaturation = Integer.parseInt(properties.getProperty("animal.health_recover"));
        this.defaultHealth = Integer.parseInt(properties.getProperty("animal.default_health"));
    }

    public Map<EntityType, CreatorService<Entity, Entity>> getTemplates() {
        return entityTemplate.getTemplates();
    }

    public CreatorService<Entity, Entity> getTemplate(EntityType type) {
        return entityTemplate.getTemplates().get(type);
    }

    public List<EntityType> getAnimals() {
        return Arrays.stream(EntityType.values())
                .filter(type -> !type.equals(EntityType.GRASS))
                .toList();
    }
}
