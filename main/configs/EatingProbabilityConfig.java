package configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import handlers.ParsingHandler;
import lombok.Getter;
import services.LivingBeing;

import java.nio.file.Path;
import java.util.Map;

@Getter
public class EatingProbabilityConfig {
    private final Map<EntityType, Map<EntityType, Integer>> probabilities;

    public EatingProbabilityConfig(Path path, ObjectMapper mapper) {
        this.probabilities = ParsingHandler.getObjectFromJson(path, new TypeReference<>(){}, mapper);
    }

    public boolean isEaten(LivingBeing attacker, Entity prey, int probability) {
        var attackerType = EntityType.ofClass(((Animal)attacker).getClass());
        var preyType = EntityType.ofClass(prey.getClass());
        if (attackerType.equals(preyType)) {
            return false;
        } else {
            return probabilities.get(attackerType).get(preyType) >= probability;
        }
    }
}
