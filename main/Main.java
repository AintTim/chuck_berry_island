import com.fasterxml.jackson.databind.ObjectMapper;
import configs.EatingProbabilityConfig;
import configs.EntityConfig;
import configs.EntityTemplateConfig;
import configs.IslandConfig;
import constants.EntityType;
import entities.Entity;
import entities.Island;
import handlers.*;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        Path generalPropertiesPath = Path.of("resources/general.properties");
        var propertiesHandler = new PropertiesHandler(generalPropertiesPath);

        var eatingProbabilityConfig = new EatingProbabilityConfig(propertiesHandler.getPath("probability.path"), new ObjectMapper());
        var templateConfig = new EntityTemplateConfig(propertiesHandler.getPath("template.path"), new ObjectMapper());

        EntityConfig entityConfig = new EntityConfig(eatingProbabilityConfig, templateConfig, propertiesHandler.getProperties());
        IslandConfig config = new IslandConfig(propertiesHandler.getProperties());

        EntityCreationHandler creator = new EntityCreationHandler(entityConfig);
        Island island = new Island(config, entityConfig);
        EntityActionHandler actionManager = new EntityActionHandler(island, entityConfig);
        EntityMovementHandler movementManager = new EntityMovementHandler(island);
        EntityEatingHandler eatingManager = new EntityEatingHandler(island, entityConfig);
        EntityBreedingHandler breedingManager = new EntityBreedingHandler(island, entityConfig);

        //Заполнение острова сущностями (животные + растения)
        creator.fillIslandWithRandomEntities(island);

        //Выбор действия на раунд
        island.prepareAnimals(actionManager::setRandomAction);

        //Перемещение сущности
        System.out.println("-------------------------------------");
        island.moveAnimals(movementManager::moveEntity);

        island.prepareAnimals(actionManager::setRandomAction);
        //Поедание сущности
        System.out.println("-------------------------------------");
        island.feedAnimals(eatingManager::feedAnimal);

        System.out.println("-------------------------------------");
        island.moveAnimals(movementManager::moveEntity);

        //Размножение животных
        System.out.println("-------------------------------------");
        island.breedAnimals(breedingManager::getRandomBreedingPartner, creator::createEntity);
        //Заполнение растениями
        island.refillPlants(diff -> creator.createEntities(EntityType.GRASS, ThreadLocalRandom.current().nextInt(diff)));

        //Удаление сущностей по условию
        island.resetEntities(Entity::getRemovable);
        System.out.println("Wolves removed");
    }
}
