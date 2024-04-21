import com.fasterxml.jackson.databind.ObjectMapper;
import configs.EatingProbabilityConfig;
import configs.EntityTemplateConfig;
import constants.EntityType;
import entities.Entity;
import handlers.EntityHandler;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path probabilityPath = Path.of("resources/EatingProbability.json");
        Path templatePath = Path.of("resources/EntityTemplates.json");
        ObjectMapper mapper = new ObjectMapper();

        EatingProbabilityConfig eatingProbabilityConfig = new EatingProbabilityConfig(probabilityPath, new ObjectMapper());
        EntityHandler handler = new EntityHandler(new EntityTemplateConfig(templatePath, mapper));
        Entity wolf = handler.createEntity(EntityType.WOLF);
        Entity wolf2 = handler.createEntity(EntityType.WOLF);
        Entity grass = handler.createEntity(EntityType.GRASS);
        System.out.println("Hello");
    }
}
