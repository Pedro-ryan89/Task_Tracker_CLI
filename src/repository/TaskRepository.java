package repository;

import model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final Path ACTIVE_TASKS_PATH = Path.of("tasks.json");
    private final Path DELETED_TASKS_PATH = Path.of("deleted_tasks.json");

    public List<Task> loadActiveTasks() {
        return loadTasksFromFile(ACTIVE_TASKS_PATH);
    }

    public List<Task> loadDeletedTasks() {
        return loadTasksFromFile(DELETED_TASKS_PATH);
    }

    public void saveAll(List<Task> active, List<Task> deleted) {
        saveTasksToFile(active, ACTIVE_TASKS_PATH);
        saveTasksToFile(deleted, DELETED_TASKS_PATH);
    }

    private List<Task> loadTasksFromFile(Path path) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try {
            String content = Files.readString(path).strip();
            if (content.isEmpty() || content.equals("[]")) {
                return new ArrayList<>();
            }

            if (content.startsWith("["))
                content = content.substring(1);
            if (content.endsWith("]"))
                content = content.substring(0, content.length() - 1);

            List<Task> tasks = new ArrayList<>();
            int braceCount = 0, start = 0;

            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == '{') {
                    if (braceCount == 0)
                        start = i;
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        tasks.add(Task.fromJson(content.substring(start, i + 1)));
                    }
                }
            }
            return tasks;

        } catch (IOException e) {
            System.err.println("Erro ao ler: " + path);
            return new ArrayList<>();
        }
    }

    private void saveTasksToFile(List<Task> tasks, Path path) {
        try {
            String json = tasks.stream()
                    .map(Task::toJson)
                    .reduce((a, b) -> a + ",\n  " + b)
                    .orElse("");

            Files.writeString(path, "[\n  " + json + "\n]");
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + path);
        }
    }
}