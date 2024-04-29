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

    void moveAnimals(Function<Animal, Field> relocate);

    void feedAnimals(Function<Animal, List<Entity>> eat);

    void prepareAnimals(Consumer<Animal> prepare);

    void breedAnimals(UnaryOperator<Animal> breed, Function<EntityType, Entity> createOffspring);
}
