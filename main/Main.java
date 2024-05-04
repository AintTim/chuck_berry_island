import com.fasterxml.jackson.databind.ObjectMapper;
import configs.EatingProbabilityConfig;
import configs.EntityConfig;
import configs.EntityTemplateConfig;
import configs.IslandConfig;
import constants.EntityType;
import constants.Stage;
import entities.Island;
import handlers.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Path generalPropertiesPath = Path.of("resources/general.properties");
        var propertiesHandler = new PropertiesHandler(generalPropertiesPath);

        var eatingProbabilityConfig = new EatingProbabilityConfig(propertiesHandler.getPath("probability.path"), new ObjectMapper());
        var templateConfig = new EntityTemplateConfig(propertiesHandler.getPath("template.path"), new ObjectMapper());

        EntityConfig entityConfig = new EntityConfig(eatingProbabilityConfig, templateConfig, propertiesHandler.getProperties());
        IslandConfig config = new IslandConfig(propertiesHandler.getProperties());

        CreationHandler creator = new CreationHandler(entityConfig);
        StatisticsHandler statistics = new StatisticsHandler(entityConfig);
        Island island = new Island(config, entityConfig, statistics);
        ActionHandler actionManager = new ActionHandler(island, entityConfig);
        MovementHandler movementManager = new MovementHandler(island);
        EatingHandler eatingManager = new EatingHandler(island, entityConfig);
        BreedingHandler breedingManager = new BreedingHandler(island, entityConfig);
        TaskHandler taskManager = new TaskHandler();

        //Заполнение острова сущностями (животные + растения)
        creator.fillIslandWithRandomEntities(island);

        int days = 1;
        Integer entities = Integer.MAX_VALUE;
        ExecutorService plantsExecutor = Executors.newSingleThreadExecutor();
        ExecutorService preparationExecutor = Executors.newCachedThreadPool();
        ExecutorService movementExecutor = Executors.newFixedThreadPool(12);
        ExecutorService feedExecutor = Executors.newFixedThreadPool(12);
        ExecutorService breedingExecutor = Executors.newFixedThreadPool(12);
        ExecutorService resetExecutor = Executors.newSingleThreadExecutor();

        while (entities != 0) {
            statistics.clearStats();
            //Подсчитываем общее количество сущностей
            island.countEntities();
            //Выращивание растений
            taskManager.runTask(plantsExecutor, new Task(Stage.PLANTING,
                    () -> island.refillPlants(diff -> creator.createEntities(EntityType.GRASS, ThreadLocalRandom.current().nextInt(diff)))));
            //Выбор действия на раунд
            taskManager.runTasks(preparationExecutor,
                    TaskHandler.assignTasks(Stage.PREPARE, entityConfig.getAnimals(), type -> island.prepareAnimals(type, actionManager::setRandomAction)));
            //Перемещение сущности
            taskManager.runTasks(movementExecutor,
                    TaskHandler.assignTasks(Stage.MOVING, entityConfig.getAnimals(), type -> island.moveAnimals(type, movementManager::moveEntity)));
            taskManager.runTasks(feedExecutor,
                    TaskHandler.assignTasks(Stage.EATING, entityConfig.getAnimals(), type -> island.feedAnimals(type, eatingManager::feedAnimal)));
            taskManager.runTasks(breedingExecutor,
                    TaskHandler.assignTasks(Stage.BREEDING, entityConfig.getAnimals(), type -> island.breedAnimals(type, breedingManager::getRandomBreedingPartner, creator::createEntity)));
            //Удаление умерших животных
            island.resetEntities(statistics::countDead);
            statistics.setCurrentDay(days++);
            statistics.printStatistics();
            entities = statistics.getAnimalsTotal();
        }
        taskManager.shutdownAll();

//        statistics.clearStats();
//        island.countEntities();
//        island.refillPlants(diff -> creator.createEntities(EntityType.GRASS, ThreadLocalRandom.current().nextInt(diff)));
//        island.prepareAnimals(actionManager::setRandomAction);
//        island.moveAnimals(movementManager::moveEntity);
//        island.feedAnimals(eatingManager::feedAnimal);
//        island.breedAnimals(breedingManager::getRandomBreedingPartner, creator::createEntity);
//        island.resetEntities(entity -> {
//            if (Boolean.TRUE.equals(entity.getRemovable())) {
//                statistics.getDead().merge(EntityType.ofClass(entity.getClass()), 1, Integer::sum);
//                return true;
//            }
//            return false;
//        });
//        statistics.setCurrentDay(days);
//        statistics.printStatistics();
        System.out.println("Итог");
    }
}
