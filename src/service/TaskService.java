package service;

import model.Status;
import model.Task;
import repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepository repository;

    private List<Task> activeTasks;
    private List<Task> deletedTasks;
    private int lastId;

    public TaskService() {
        this.repository = new TaskRepository();

        this.activeTasks = repository.loadActiveTasks();
        this.deletedTasks = repository.loadDeletedTasks();

        runMaintenance();
        updateLastId();
    }

    private void updateLastId() {
        int max1 = activeTasks.stream().mapToInt(Task::getId).max().orElse(0);
        int max2 = deletedTasks.stream().mapToInt(Task::getId).max().orElse(0);
        this.lastId = Math.max(max1, max2);
    }

    private void save() {
        repository.saveAll(activeTasks, deletedTasks);
    }

    public void addTask(String description) {
        Task task = new Task(++lastId, description);
        activeTasks.add(task);
        save();
    }

    public Optional<Task> findById(int id) {
        return activeTasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public boolean updateStatus(int id, Status status) {
        Optional<Task> task = findById(id);
        if (task.isPresent()) {
            task.get().setStatus(status);
            save();
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        Optional<Task> task = findById(id);
        if (task.isPresent()) {
            Task t = task.get();
            activeTasks.remove(t);
            t.setDeletedAt(LocalDateTime.now());
            deletedTasks.add(t);
            save();
            return true;
        }
        return false;
    }

    private void runMaintenance() {
        activeTasks.stream()
                .filter(t -> t.getStatus() == Status.DONE &&
                        ChronoUnit.HOURS.between(t.getUpdatedAt(), LocalDateTime.now()) >= 24)
                .toList()
                .forEach(t -> delete(t.getId()));

        deletedTasks.removeIf(t -> t.getDeletedAt() != null &&
                ChronoUnit.DAYS.between(t.getDeletedAt(), LocalDateTime.now()) >= 7);
    }

    public List<Task> getActive() {
        return List.copyOf(activeTasks);
    }

    public List<Task> getDeleted() {
        return List.copyOf(deletedTasks);
    }

    public void emptyTrash() {
        deletedTasks.clear();
        save();
    }
}