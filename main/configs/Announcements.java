package configs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Announcements {
    public final String DAY_COUNTER = "=========== %s День %s (%s) ==========%n%n";
    public final String LINE_SPLITTER = "=================================\n";
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
    public final String KEEP_CURRENT_SETTINGS = "Желаете сохранить текущие настройки?\nВведите что-нибудь для изменения настроек";
    public final String SETTINGS_LIST = "Доступные параметры: ";
    public final String INVALID_SETTING = "Выбранный параметр %s отсутствует в списке доступных.\nПожалуйста, выберите существующую настройку:\n";
    public final String INVALID_VALUE = "Новое значение должно являться числом";
    public final String CHANGE_SETTINGS = "Для изменения настройки введите название желаемого параметра: ";
    public final String SET_SETTING_VALUE = "Введите новое значение для параметра %s (текущее значение - %d):\n";
    public final String KEEP_CHANGING_SETTINGS = "Продолжить изменения настроек?\nY/N";
    public final String RUN_SIMULATION_DEFAULT_SETTINGS = "Запускаем симуляцию с текущими настройками";
}
