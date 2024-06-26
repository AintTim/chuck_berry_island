package handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class ParsingHandler {

    public static <T> T getObjectFromJson(Path path, TypeReference<T> typeReference, ObjectMapper mapper) {
        try {
            if (Files.exists(path)) {
                return mapper.readValue(path.toFile(), typeReference);
            } else {
                throw new IllegalArgumentException("Искомый файл не существует по указанному пути");
            }
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Невозможно прочитать указанный файл - %s", path));
        }
    }
}
