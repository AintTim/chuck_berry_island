package entities.plants;

import entities.Entity;
import lombok.NoArgsConstructor;
import services.CreatorService;

@NoArgsConstructor
public abstract class Plant extends Entity implements CreatorService<Plant, Plant> {

    protected Plant(Double weight, Integer velocity, Integer limit, Double hunger, String picture) {
        super(weight, velocity, limit, hunger, picture);
    }

    @Override
    public String toString() {
        return String.format("%d%s", id, picture);
    }
}
