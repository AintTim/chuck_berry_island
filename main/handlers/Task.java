package handlers;

import constants.EntityType;
import constants.Stage;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class Task implements Callable<Task> {
    private final Stage stage;
    private final EntityType type;
    private Consumer<EntityType> consumer;
    private Runnable runnable;


    public Task(Stage stage, EntityType type, Consumer<EntityType> task) {
        this.stage = stage;
        this.type = type;
        this.consumer = task;
    }

    public Task(Stage stage, Runnable task) {
        this.stage = stage;
        this.type = EntityType.GRASS;
        this.runnable = task;
    }

    @Override
    public Task call() throws Exception {
        var start = System.currentTimeMillis();
        if (Objects.nonNull(consumer)) {
            consumer.accept(type);
        } else {
            runnable.run();
        }
//        System.out.printf("%s: %s подготовлен за %d%n", stage, type, System.currentTimeMillis() - start);
        return this;
    }
}
