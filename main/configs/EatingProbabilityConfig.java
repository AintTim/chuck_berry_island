package configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.EntityType;
import entities.Animal;
import handlers.ParsingHandler;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Getter
public class EatingProbabilityConfig {
    private final ConcurrentMap<EntityType, Map<EntityType, Integer>> probabilities;

    public EatingProbabilityConfig(Path path, ObjectMapper mapper) {
        this.probabilities = ParsingHandler.getObjectFromJson(path, new TypeReference<>(){}, mapper);
    }

    public List<EntityType> getPreys(Animal animal) {
        EntityType type = EntityType.ofClass(animal.getClass());
        return probabilities.get(type).entrySet().stream()
                .filter(map -> map.getValue() > 0)
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean canBeEaten(Animal attacker, EntityType prey, int probability) {
        var attackerType = EntityType.ofClass(attacker.getClass());
        return probabilities.get(attackerType).get(prey) >= probability;
    }
}
