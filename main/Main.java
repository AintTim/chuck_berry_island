import com.fasterxml.jackson.databind.ObjectMapper;
import configs.EatingProbabilityConfig;
import configs.EntityConfig;
import configs.EntityTemplateConfig;
import configs.IslandConfig;
import constants.EntityType;
import entities.Entity;
import entities.Island;
import entities.predators.Wolf;
import handlers.EntityCreationHandler;
import handlers.PropertiesHandler;

import java.nio.file.Path;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Path generalPropertiesPath = Path.of("resources/general.properties");
        var propertiesHandler = new PropertiesHandler(generalPropertiesPath);

        var eatingProbabilityConfig = new EatingProbabilityConfig(propertiesHandler.getPath("probability.path"), new ObjectMapper());
        var templateConfig = new EntityTemplateConfig(propertiesHandler.getPath("template.path"), new ObjectMapper());

        EntityConfig entityConfig = new EntityConfig(eatingProbabilityConfig, templateConfig);
        IslandConfig config = new IslandConfig(propertiesHandler.getProperties());


        EntityCreationHandler creator = new EntityCreationHandler(entityConfig);
        Island island = new Island(config);
        island.refillPlants(entityConfig.getTemplate(EntityType.GRASS), diff -> creator.createEntities(EntityType.GRASS, new Random().nextInt(diff)));

        System.out.println("Hello");
    }
}
