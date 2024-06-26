package configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.EntityType;
import entities.Entity;
import handlers.ParsingHandler;
import lombok.Getter;
import services.CreatorService;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentMap;

@Getter
public class EntityTemplateConfig {

    private final ConcurrentMap<EntityType, CreatorService<Entity, Entity>> templates;

    public EntityTemplateConfig(Path path, ObjectMapper mapper) {
        templates = ParsingHandler.getObjectFromJson(path, new TypeReference<>(){}, mapper);
    }
}
