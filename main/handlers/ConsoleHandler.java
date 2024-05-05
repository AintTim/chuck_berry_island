package handlers;

import configs.Announcements;

import java.util.Scanner;

public class ConsoleHandler {
    private final Scanner scanner;
    private final PropertiesHandler properties;
    private final String islandPicture = "\uD83C\uDFDD️";

    public ConsoleHandler(Scanner scanner, PropertiesHandler propertiesHandler) {
        this.scanner = scanner;
        this.properties = propertiesHandler;
    }

    public void announce(String announcement) {
        System.out.println(announcement);
    }

    public void announce(String announcement, Object... params) {
        System.out.printf(announcement, params);
    }

    public void presentOptions() {
        announce(Announcements.CURRENT_SETUP);
        announce("\uD83C\uDFDD Остров:");
        int width =  properties.getNumberProperty("island.width");
        int height =  properties.getNumberProperty("island.height");
        announce(Announcements.ISLAND_SQUARE, Math.multiplyExact(width, height), width, height);
        announce(Announcements.ISLAND_LIFESPAN, properties.getNumberProperty("island.lifespan"));
        announce("\uD83E\uDDF8 Животные:");
        announce(Announcements.ANIMAL_HEALTH, properties.getNumberProperty("animal.default_health"));
        announce(Announcements.ANIMAL_HEALTH_RECOVER, properties.getNumberProperty("animal.health_recover"));
        announce(Announcements.ANIMAL_HEALTH_REDUCTION, properties.getNumberProperty("animal.health_reduction"));
    }
}
