package handlers;

import configs.Announcements;
import configs.EntityConfig;
import constants.EntityType;
import entities.Animal;
import entities.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class StatisticsHandler {
    private static final String PAW = "\uD83D\uDC3E";
    private static final String COFFIN = "⚰️";
    private static final String DAY = "\uD83D\uDCC5";

    @Setter
    private int currentDay;
    private final EntityConfig config;
    private final ConcurrentMap<EntityType, Integer> total;
    private final ConcurrentMap<EntityType, Integer> born;
    private final ConcurrentMap<EntityType, Integer> dead;
    private final ConcurrentMap<EntityType, Integer> animalsEaten;
    private final ConcurrentMap<EntityType, Integer> plantsEaten;
    private final AtomicLong plantsEatenNumber;
    private final AtomicLong plantsGrown;
    private final ConcurrentMap<EntityType, Integer> relocated;

    public StatisticsHandler(EntityConfig config) {
        this.total = new ConcurrentHashMap<>();
        this.born = new ConcurrentHashMap<>();
        this.dead = new ConcurrentHashMap<>();
        this.animalsEaten = new ConcurrentHashMap<>();
        this.plantsEaten = new ConcurrentHashMap<>();
        this.plantsEatenNumber = new AtomicLong(0L);
        this.plantsGrown = new AtomicLong(0L);
        this.relocated = new ConcurrentHashMap<>();
        this.config = config;
    }

    public synchronized void clearStats() {
        total.clear();
        born.clear();
        dead.clear();
        animalsEaten.clear();
        plantsEaten.clear();
        plantsEatenNumber.set(0L);
        plantsGrown.set(0L);
        relocated.clear();
    }

    public synchronized void updatePlantStat(Animal animal, int value) {
        EntityType type = EntityType.ofClass(animal.getClass());
        plantsEatenNumber.addAndGet(value);
        if (Objects.isNull(plantsEaten.get(type))) {
            plantsEaten.put(type, value);
        } else {
            plantsEaten.computeIfPresent(type, (e,v) -> Integer.sum(v, value));
        }
    }

    public synchronized void printStatistics(Future<Task> result) throws InterruptedException {
        while (!result.isDone()) {
            Thread.sleep(500);
        }
        System.out.printf(Announcements.DAY_COUNTER, DAY, currentDay);
        printAnimalInfo(PAW);
        printDeadAnimalInfo(COFFIN);
        printPlantInfo(getPicture(EntityType.GRASS), count(total, true));
    }

    public boolean countDead(Entity animal) {
        if (Boolean.TRUE.equals(animal.getRemovable())) {
            dead.merge(EntityType.ofClass(animal.getClass()), 1, Integer::sum);
            return true;
        } else {
            return false;
        }
    }

    public Integer getAnimalsTotal() {
        return count(total, false);
    }

    private String getPicture(EntityType type) {
        return ((Entity)config.getTemplate(type)).getPicture();
    }

    private void printPlantInfo(String linePicture, int grassAmount) {
        printLine(linePicture);
        System.out.printf(Announcements.DAY_START, getPicture(EntityType.GRASS), grassAmount);
        System.out.printf(Announcements.GROWN_PLANTS, getPicture(EntityType.GRASS), plantsGrown.get());
        System.out.printf(Announcements.EATEN_PLANTS, getPicture(EntityType.GRASS), plantsEatenNumber.get());
        System.out.printf(Announcements.REMAINING_PLANTS, getPicture(EntityType.GRASS), (grassAmount + plantsGrown.get() - plantsEatenNumber.get()));
        System.out.println();
        print(plantsEaten, Announcements.PLANT_EATERS_COUNTER);
        printLine(linePicture);
    }

    private void printDeadAnimalInfo(String linePicture) {
        printLine(linePicture);
        System.out.printf(Announcements.DEATH_COUNTER, (count(dead) + count(animalsEaten)));
        print(dead, Announcements.DEAD_COUNTER);
        print(animalsEaten, Announcements.EATEN_COUNTER);
        printLine(linePicture);
    }

    private void printAnimalInfo(String linePicture) {
        printLine(linePicture);
        System.out.printf(Announcements.ANIMALS_COUNTER, count(total, false));
        print(total, Announcements.ANIMAL_COUNTER);
        System.out.printf(Announcements.BIRTH_COUNTER, count(born));
        print(born, Announcements.BORN_COUNTER);
        printLine(linePicture);
    }

    private void printLine(String picture) {
        System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n%n", picture, picture, picture, picture, picture, picture, picture, picture, picture);
    }

    private void print(ConcurrentMap<EntityType, Integer> map, String text) {
        Comparator<Map.Entry<EntityType, Integer>> sortByNumber = Comparator.comparingDouble(Map.Entry::getValue);

        map.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(EntityType.GRASS))
                .sorted(sortByNumber.reversed())
                .forEach(entity -> System.out.printf("%s %s %d%n", getPicture(entity.getKey()), text, entity.getValue()));
        System.out.println();
    }

    private int count(ConcurrentMap<EntityType, Integer> entities) {
        return entities.values().stream().reduce(Integer::sum).orElse(0);
    }

    private int count(ConcurrentMap<EntityType, Integer> entities, Boolean includeGrass) {
        return entities.entrySet().stream()
                .filter(e -> includeGrass.equals(e.getKey().equals(EntityType.GRASS)))
                .map(Map.Entry::getValue)
                .reduce(Integer::sum)
                .orElse(0);
    }
}
