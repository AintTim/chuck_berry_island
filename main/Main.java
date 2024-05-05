import com.fasterxml.jackson.databind.ObjectMapper;
import configs.*;
import constants.Stage;
import handlers.ConsoleHandler;
import handlers.PropertiesHandler;
import handlers.Task;
import handlers.TaskHandler;

import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Path generalPropertiesPath = Path.of("resources/general.properties");
        var propertiesHandler = new PropertiesHandler(generalPropertiesPath);
        var eatingProbabilityConfig = new EatingProbabilityConfig(propertiesHandler.getPath("probability.path"), new ObjectMapper());
        var templateConfig = new EntityTemplateConfig(propertiesHandler.getPath("template.path"), new ObjectMapper());

        ConsoleHandler console = new ConsoleHandler(new Scanner(System.in), propertiesHandler);
        console.presentOptions();
        EntityConfig entityConfig = new EntityConfig(eatingProbabilityConfig, templateConfig, propertiesHandler.getProperties());
        IslandConfig config = new IslandConfig(propertiesHandler.getProperties());
        SetupConfig setup = new SetupConfig(entityConfig, config);

        setup.init();


        int days = 1;
        Integer entities = Integer.MAX_VALUE;
        TaskHandler taskManager = new TaskHandler();
        ExecutorService plantsExecutor = Executors.newSingleThreadExecutor();
        ExecutorService preparationExecutor = Executors.newCachedThreadPool();
        ExecutorService movementExecutor = Executors.newFixedThreadPool(12);
        ExecutorService feedExecutor = Executors.newFixedThreadPool(12);
        ExecutorService breedingExecutor = Executors.newFixedThreadPool(12);
        ExecutorService resetExecutor = Executors.newSingleThreadExecutor();

        while (days < 11) {
            setup.resetStatistics();
            setup.getIsland().countEntities();
            taskManager.runTask(plantsExecutor, new Task(Stage.PLANTING, setup::updatePlants));
            taskManager.runTasks(preparationExecutor,
                    TaskHandler.assignTasks(Stage.PREPARE, entityConfig.getAnimals(), setup::prepareAnimals));
            taskManager.runTasks(movementExecutor,
                    TaskHandler.assignTasks(Stage.MOVING, entityConfig.getAnimals(), setup::moveAnimals));
            taskManager.runTasks(feedExecutor,
                    TaskHandler.assignTasks(Stage.EATING, entityConfig.getAnimals(), setup::feedAnimals));
            taskManager.runTasks(breedingExecutor,
                    TaskHandler.assignTasks(Stage.BREEDING, entityConfig.getAnimals(), setup::breedAnimals));
            setup.printStatistics(taskManager.runTask(resetExecutor, new Task(Stage.REMOVING, setup::updateAnimals)), days++);
            entities = setup.getStatisticsHandler().getAnimalsTotal();
        }
        taskManager.shutdownAll();
    }
}
