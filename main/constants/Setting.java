package constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Setting {
    WIDTH("island.width", "\uD83C\uDDFD", "Ширина"),
    HEIGHT("island.height", "\uD83C\uDDFE", "Высота"),
    LIFESPAN("island.lifespan", "⌛", "Продолжительность симуляции"),
    HEALTH("animal.default_health", "❤", "Начальное здоровье"),
    REDUCTION("animal.health_reduction", "\uD83D\uDD3B", "Уменьшение здоровья, когда животное голодно"),
    RECOVER("animal.health_recover", "\uD83D\uDD3A", "Восстановление здоровья, когда животное сыто");

    private final String property;
    private final String image;
    private final String description;

    Setting(String property, String image, String description) {
        this.property = property;
        this.image = image;
        this.description = description;
    }

    public static Optional<Setting> get(String name) {
        return Arrays.stream(Setting.values())
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
