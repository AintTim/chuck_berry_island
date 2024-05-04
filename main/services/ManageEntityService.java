package services;

import constants.EntityType;
import entities.Animal;
import entities.Entity;
import entities.Field;

import java.util.List;
import java.util.function.*;

public interface ManageEntityService {

    void resetEntities(Predicate<Entity> condition);

    void refillPlants(IntFunction<List<Entity>> createPlant);

    void moveAnimals(EntityType type, Function<Animal, Field> relocate);

    void feedAnimals(EntityType type, Function<Animal, List<Entity>> eat);

    void prepareAnimals(EntityType type, Consumer<Animal> prepare);

    void breedAnimals(EntityType type, UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring);
}
