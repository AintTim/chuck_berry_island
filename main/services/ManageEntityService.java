package services;

import entities.Entity;
import entities.plants.Plant;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public interface ManageEntityService {

    void removeEntities(Predicate<Entity> condition);

    void refillPlants(Entity plant, IntFunction<List<Entity>> createPlant);
}
