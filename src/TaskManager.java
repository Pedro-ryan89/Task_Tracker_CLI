import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskManager {

    private final Path ACTIVE_TASKS_PATH = Path.of("tasks.json");
    private final Path DELETED_TASKS_PATH = Path.of("deleted_tasks.json");

    private List<Task> activeTasks;
    private List<Task> deletedTasks;
    private int lastId = 0;

    public TaskManager() {
        this.activeTasks = loadTasksFromFile(ACTIVE_TASKS_PATH);
        this.deletedTasks = loadTasksFromFile(DELETED_TASKS_PATH);

        boolean changesMade = runMaintenance();

        updateLastId();

        if (changesMade) {
            saveAllTasks();
        }
    }

    private void updateLastId() {
        int maxActiveId = this.activeTasks.stream().mapToInt(Task::getId).max().orElse(0);
        int maxDeletedId = this.deletedTasks.stream().mapToInt(Task::getId).max().orElse(0);
        this.lastId = Math.max(maxActiveId, maxDeletedId);
    }

    private boolean runMaintenance() {
        boolean changesOccurred = false;

        List<Task> tasksToMove = this.activeTasks.stream()
                .filter(task -> task.getStatus() == Status.DONE && ChronoUnit.HOURS.between(task.getUpdatedAt(), LocalDateTime.now()) >= 24)
                .collect(Collectors.toList());

        if (!tasksToMove.isEmpty()) {
            tasksToMove.forEach(task -> deleteTask(task.getId()));
            changesOccurred = true;
        }

        boolean removedFromTrash = this.deletedTasks.removeIf(task ->
                task.getDeletedAt() != null && ChronoUnit.DAYS.between(task.getDeletedAt(), LocalDateTime.now()) >= 7
        );

        if (removedFromTrash) {
            changesOccurred = true;
        }

        return changesOccurred;
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
            
            List<Task> loadedTasks = new ArrayList<>();
            int braceCount = 0;
            int start = -1;
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == '{') {
                    if (braceCount == 0) start = i;
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0 && start != -1) {
                        loadedTasks.add(Task.fromJson(content.substring(start, i + 1)));
                        start = -1;
                    }
                }
            }
            return loadedTasks;
        } catch (IOException e) {
            System.err.println("Error reading file: " + path + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveTasksToFile(List<Task> tasks, Path path) {
        try {
            String jsonTasks = tasks.stream()
                    .map(Task::toJson)
                    .collect(Collectors.joining(",\n  "));
            String jsonArray = "[\n  " + jsonTasks + "\n]";
            Files.writeString(path, jsonArray);
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + path + " - " + e.getMessage());
        }
    }

    private void saveAllTasks() {
        saveTasksToFile(activeTasks, ACTIVE_TASKS_PATH);
        saveTasksToFile(deletedTasks, DELETED_TASKS_PATH);
    }

    public List<Task> getActiveTasks() {
        return List.of(this.activeTasks.toArray(new Task[0]));
    }

    public List<Task> getDeletedTasks() {
        return List.of(this.deletedTasks.toArray(new Task[0]));
    }

    public void addTask(String description) {
        this.lastId++;
        Task newTask = new Task(this.lastId, description);
        this.activeTasks.add(newTask);
        saveAllTasks();
    }

    public Optional<Task> findTaskById(int id) {
        return this.activeTasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public boolean updateTaskStatus(int id, Status newStatus) {
        Optional<Task> taskToUpdate = findTaskById(id);
        if (taskToUpdate.isPresent()) {
            taskToUpdate.get().setStatus(newStatus);
            saveAllTasks();
            return true;
        }
        return false;
    }

    public boolean deleteTask(int id) {
        Optional<Task> taskToMove = findTaskById(id);
        if (taskToMove.isPresent()) {
            Task task = taskToMove.get();
            this.activeTasks.remove(task);
            task.setDeletedAt(LocalDateTime.now());
            this.deletedTasks.add(task);
            saveAllTasks();
            return true;
        }
        return false;
    }
}