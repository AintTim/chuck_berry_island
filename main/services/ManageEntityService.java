package services;

import entities.Animal;
import entities.Entity;
import entities.Field;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public interface ManageEntityService {

    void resetEntities(Predicate<Entity> condition);

    void refillPlants(Entity plant, IntFunction<List<Entity>> createPlant);

    void moveAnimals(Function<Animal, Field> relocate);

    void feedAnimals(Function<Animal, List<Entity>> eat);
}
