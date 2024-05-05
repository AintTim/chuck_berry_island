package configs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Announcements {
    public final String DAY_COUNTER = "=========== %s День %s ==========%n%n";
    public final String ANIMALS_COUNTER = "Всего животных - %s%n";
    public final String ANIMAL_COUNTER = "всего:";
    public final String BIRTH_COUNTER = "Всего родилось на острове - %s%n";
    public final String DEATH_COUNTER = "Всего умерло на острове - %s%n";
    public final String EATEN_COUNTER = "было съедено:";
    public final String DEAD_COUNTER = "умерло от голода:";
    public final String BORN_COUNTER = "родилось:";
    public final String PLANT_EATERS_COUNTER = "съедено травы:";
    public final String DAY_START = "%s Начало дня - %s%n";
    public final String GROWN_PLANTS = "%s Выращено: %s%n";
    public final String EATEN_PLANTS = "%s Съедено: %s%n";
    public final String REMAINING_PLANTS = "%s Осталось: %s%n";
    public final String CURRENT_SETUP = "⚙️ Текущие настройки симуляции:";
    public final String ISLAND_SQUARE = "\t\uD83D\uDFE9 Общая площадь - %d (%d:%d)%n";
    public final String ISLAND_LIFESPAN = "\t⌛ Продолжительность симуляции - %d дней%n";
    public final String ANIMAL_HEALTH = "\t❤ Начальное здоровье - %d%n";
    public final String ANIMAL_HEALTH_RECOVER = "\t\uD83D\uDD3A Восстановление здоровья, когда животное сыто - %d%n";
    public final String ANIMAL_HEALTH_REDUCTION = "\t\uD83D\uDD3B Уменьшение здоровья, когда животное голодно - %d%n";
}
