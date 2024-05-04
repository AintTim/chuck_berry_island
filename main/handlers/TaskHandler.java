package handlers;

import constants.EntityType;
import constants.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class TaskHandler {
    private final List<ExecutorService> executors = new ArrayList<>();

    public static List<Task> assignTasks(Stage stage, List<EntityType> entities, Consumer<EntityType> function) {
        List<Task> list = new ArrayList<>();
        entities.forEach(type -> list.add(new Task(stage, type, function)));
        return list;
    }

    public Future<Task> runTask(ExecutorService executor, Task task) {
        executors.add(executor);
        return executor.submit(task);
    }

    public List<Future<Task>> runTasks(ExecutorService executor, List<Task> tasks) {
        try {
            executors.add(executor);
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.out.printf("Выполнение задач %s было прервано%n", tasks.get(0).getStage());
            throw new RuntimeException(e.getCause());
        }
    }

    public void shutdownAll() {
        executors.forEach(ExecutorService::shutdown);
    }
}
