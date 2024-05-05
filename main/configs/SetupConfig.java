package configs;

import constants.EntityType;
import entities.Island;
import handlers.*;
import lombok.Getter;

import java.time.Instant;
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
        creationHandler.fillIslandWithEntities(island);
    }

    public void printStatistics(Future<Task> updateAnimals, int currentDay) {
        statisticsHandler.setCurrentDay(currentDay);
        statisticsHandler.setEnd(Instant.now());
        try {
            statisticsHandler.printStatistics(updateAnimals);
        } catch (InterruptedException e) {
            System.out.println("Выполнение задачи было прервано или выбросило исключение");
            throw new RuntimeException(e.getMessage());
        }
    }

    public void resetStatistics() {
        statisticsHandler.setStart(Instant.now());
        statisticsHandler.clearStats();
    }

    public void prepareAnimals(EntityType type) {
        island.prepareAnimals(type, actionHandler::setRandomAction);
    }

    public void feedAnimals(EntityType type) {
        island.feedAnimals(type, eatingHandler::feedAnimal);
    }

    public void breedAnimals(EntityType type) {
        island.breedAnimals(type, breedingHandler::getRandomBreedingPartner, creationHandler::createEntity);
    }

    public void moveAnimals(EntityType type) {
        island.moveAnimals(type, movementHandler::moveEntity);
    }

    public void updatePlants() {
        island.refillPlants(diff -> creationHandler.createEntities(EntityType.GRASS, ThreadLocalRandom.current().nextInt(diff)));
    }

    public void updateAnimals() {
        island.resetEntities(statisticsHandler::countDead);
    }
}
