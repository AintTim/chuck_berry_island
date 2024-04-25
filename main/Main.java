import com.fasterxml.jackson.databind.ObjectMapper;
import configs.EatingProbabilityConfig;
import configs.EntityConfig;
import configs.EntityTemplateConfig;
import configs.IslandConfig;
import constants.Action;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;
import entities.Island;
import entities.predators.Wolf;
import handlers.EntityActionHandler;
import handlers.EntityCreationHandler;
import handlers.EntityMovementHandler;
import handlers.PropertiesHandler;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Random;
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
        EntityActionHandler actionManager = new EntityActionHandler(island);
        EntityMovementHandler movementManager = new EntityMovementHandler(island);

        //Заполнение острова сущностями (животные + растения)
        creator.fillIslandWithRandomEntities(island, ThreadLocalRandom.current());

        //Выбор действия на раунд
        actionManager.prepareEntities();

        //Перемещение сущности
        island.moveAnimals(movementManager::moveEntity);


        //Заполнение растениями
//        island.refillPlants(entityConfig.getTemplate(EntityType.GRASS), diff -> creator.createEntities(EntityType.GRASS, new Random().nextInt(diff)));

        //Удаление сущностей по условию
        island.resetEntities(Entity::getIsRemovable);
        System.out.println("Wolves removed");
    }
}
