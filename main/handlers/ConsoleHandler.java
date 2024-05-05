package handlers;

import configs.Announcements;
import constants.Setting;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

import static constants.Setting.*;

public class ConsoleHandler {
    private final Scanner scanner;
    private final PropertiesHandler properties;

    public ConsoleHandler(Scanner scanner, PropertiesHandler propertiesHandler) {
        this.scanner = scanner;
        this.properties = propertiesHandler;
    }

    public void printCurrentSettings() {
        announce(Announcements.CURRENT_SETUP);
        announce("\uD83C\uDFDD Остров:");
        int width = properties.getNumberProperty(WIDTH.getProperty());
        int height = properties.getNumberProperty(HEIGHT.getProperty());
        announce(Announcements.ISLAND_SQUARE, Math.multiplyExact(width, height), width, height);
        announce("\t%s %s - %d дней%n", LIFESPAN.getImage(), LIFESPAN.getDescription(), properties.getNumberProperty(LIFESPAN.getProperty()));
        announce("\uD83E\uDDF8 Животные:");
        String commonMessageSchema = "\t%s %s - %d%n";
        announce(commonMessageSchema, HEALTH.getImage(), HEALTH.getDescription(), properties.getNumberProperty(HEALTH.getProperty()));
        announce(commonMessageSchema, RECOVER.getImage(), RECOVER.getDescription(), properties.getNumberProperty(RECOVER.getProperty()));
        announce(commonMessageSchema, REDUCTION.getImage(), REDUCTION.getDescription(), properties.getNumberProperty(REDUCTION.getProperty()));
        System.out.println();
    }

    public void editSettings() {
        if (changeSettings()) {
            do {
                Setting setting = selectSetting();
                setSettingValue(setting);
                printCurrentSettings();
            } while (continueSettingsChange());
            announce(Announcements.RUN_SIMULATION_DEFAULT_SETTINGS);
            printCurrentSettings();
        }
    }

    private void announce(String announcement) {
        System.out.println(announcement);
    }

    private void announce(String announcement, Object... params) {
        System.out.printf(announcement, params);
    }

    private boolean changeSettings() {
        announce(Announcements.KEEP_CURRENT_SETTINGS);
        String input = scanner.nextLine();
        if (input.isBlank()) {
            announce(Announcements.LINE_SPLITTER);
            announce(Announcements.RUN_SIMULATION_DEFAULT_SETTINGS);
            return false;
        }
        return true;
    }

    private void setSettingValue(Setting setting) {
        announce(Announcements.SET_SETTING_VALUE, setting, properties.getNumberProperty(setting.getProperty()));
        String input = scanner.nextLine();
        while (!validateValue(input)) {
            announce(Announcements.INVALID_VALUE);
            input = scanner.nextLine();
        }
        properties.getProperties().setProperty(setting.getProperty(), input);
    }

    private boolean continueSettingsChange() {
        announce(Announcements.KEEP_CHANGING_SETTINGS);
        return scanner.nextLine().equalsIgnoreCase("Y");
    }

    private Setting selectSetting() {
        announce(Announcements.SETTINGS_LIST);
        presentAvailableSettings();
        announce(Announcements.CHANGE_SETTINGS);
        String input = scanner.nextLine();
        Optional<Setting> setting = Setting.get(input);
        while (setting.isEmpty()) {
            announce(Announcements.INVALID_SETTING, input);
            input = scanner.nextLine();
            setting = Setting.get(input);
        }
        return setting.get();
    }

    private void presentAvailableSettings() {
        Arrays.stream(Setting.values())
                .forEach(s -> announce(s.name().toLowerCase()));
    }

    private boolean validateValue(String value) {
        if (value.isBlank()) {
            return false;
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
