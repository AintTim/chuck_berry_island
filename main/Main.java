import com.fasterxml.jackson.databind.ObjectMapper;
import configs.*;
import constants.Setting;
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
        console.printCurrentSettings();
        console.editSettings();

        EntityConfig entityConfig = new EntityConfig(eatingProbabilityConfig, templateConfig, propertiesHandler.getProperties());
        IslandConfig config = new IslandConfig(propertiesHandler.getProperties());
        SetupConfig setup = new SetupConfig(entityConfig, config);

        TaskHandler taskManager = new TaskHandler();
        setup.init();

        ExecutorService plantsExecutor = Executors.newSingleThreadExecutor();
        ExecutorService preparationExecutor = Executors.newCachedThreadPool();
        ExecutorService movementExecutor = Executors.newFixedThreadPool(entityConfig.getAnimals().size());
        ExecutorService feedExecutor = Executors.newFixedThreadPool(entityConfig.getAnimals().size());
        ExecutorService breedingExecutor = Executors.newFixedThreadPool(entityConfig.getAnimals().size());
        ExecutorService resetExecutor = Executors.newSingleThreadExecutor();

        int days = 0;
        while (days < propertiesHandler.getNumberProperty(Setting.LIFESPAN.getProperty())) {
            setup.resetStatistics();
            setup.getIsland().countEntities();
            taskManager.runTask(plantsExecutor, new Task(Stage.PLANTING, setup::updatePlants));
            taskManager.runTasks(preparationExecutor, TaskHandler.assignTasks(Stage.PREPARE, entityConfig.getAnimals(), setup::prepareAnimals));
            taskManager.runTasks(movementExecutor, TaskHandler.assignTasks(Stage.MOVING, entityConfig.getAnimals(), setup::moveAnimals));
            taskManager.runTasks(feedExecutor, TaskHandler.assignTasks(Stage.EATING, entityConfig.getAnimals(), setup::feedAnimals));
            taskManager.runTasks(breedingExecutor, TaskHandler.assignTasks(Stage.BREEDING, entityConfig.getAnimals(), setup::breedAnimals));
            setup.printStatistics(taskManager.runTask(resetExecutor, new Task(Stage.REMOVING, setup::updateAnimals)), days++);
        }
        taskManager.shutdownAll();
    }
}
