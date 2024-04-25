import com.fasterxml.jackson.databind.ObjectMapper;
import configs.EatingProbabilityConfig;
import configs.EntityConfig;
import configs.EntityTemplateConfig;
import configs.IslandConfig;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;
import entities.Island;
import entities.predators.Wolf;
import handlers.EntityCreationHandler;
import handlers.EntityMovementHandler;
import handlers.PropertiesHandler;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

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
        EntityMovementHandler movementManager = new EntityMovementHandler(island, templateConfig);
        creator.fillIslandWithRandomEntities(island, ThreadLocalRandom.current());
        Field start = new Field(0, 0);
        int count = 0;
        for (Entity wolf : island.getFields().get(start).get(EntityType.WOLF)) {
            island.moveAnimal(start, wolf, movementManager::moveEntity);
            count++;
        }
        System.out.println(count + " волков было перемещено");

        island.resetEntities(entity -> entity instanceof Wolf animal && animal.getIsRemovable());
        System.out.println("Wolves removed");
    }
}
