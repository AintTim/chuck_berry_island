package configs;

import constants.EntityType;
import entities.Island;
import handlers.*;
import lombok.Getter;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class SetupConfig {
    private final CreationHandler creationHandler;
    private final ActionHandler actionHandler;
    private final MovementHandler movementHandler;
    private final EatingHandler eatingHandler;
    private final BreedingHandler breedingHandler;
    private final StatisticsHandler statisticsHandler;
    private final Island island;

    public SetupConfig(EntityConfig entityConfig, IslandConfig islandConfig) {
        creationHandler = new CreationHandler(entityConfig);
        statisticsHandler = new StatisticsHandler(entityConfig);
        island = new Island(islandConfig, entityConfig, statisticsHandler);
        actionHandler = new ActionHandler(island, entityConfig);
        movementHandler = new MovementHandler(island);
        eatingHandler = new EatingHandler(island, entityConfig);
        breedingHandler = new BreedingHandler(island, entityConfig);
    }

    public void init() {
        System.out.println("Заполняем остров...");
        creationHandler.fillIslandWithRandomEntities(island);
        System.out.println("Остров заполнен");
    }

    public void printStatistics(Future<Task> updateAnimals, int currentDay) {
        statisticsHandler.setCurrentDay(currentDay);
        try {
            statisticsHandler.printStatistics(updateAnimals);
        } catch (InterruptedException e) {
            System.out.println("Выполнение задачи было прервано или выбросило исключение");
            throw new RuntimeException(e.getMessage());
        }
    }

    public void resetStatistics() {
        statisticsHandler.clearStats();
    }

    public void prepareAnimals(EntityType type) {
        island.prepareAnimals(type, actionHandler::setRandomAction);
    }

    public void feedAnimals(EntityType type) {
        System.out.println("Кормим животных...");
        island.feedAnimals(type, eatingHandler::feedAnimal);
        System.out.println("Животные накормлены");
    }

    public void breedAnimals(EntityType type) {
        System.out.println("Разводим животных...");
        island.breedAnimals(type, breedingHandler::getRandomBreedingPartner, creationHandler::createEntity);
        System.out.println("Животные расплодились");
    }

    public void moveAnimals(EntityType type) {
        System.out.println("Перемещаем животных...");
        island.moveAnimals(type, movementHandler::moveEntity);
        System.out.println("Животные перемещены");
    }

    public void updatePlants() {
        System.out.println("Выращиваем растения...");
        island.refillPlants(diff -> creationHandler.createEntities(EntityType.GRASS, ThreadLocalRandom.current().nextInt(diff)));
        System.out.println("Растения выращены");
    }

    public void updateAnimals() {
        island.resetEntities(statisticsHandler::countDead);
    }
}
