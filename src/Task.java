import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Task {
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public int getId() { return id; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }


    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }


    public String toJson() {
        return "{"
                + "\"id\": " + id + ", "
                + "\"description\": \"" + escapeJson(description.strip()) + "\", "
                + "\"status\": \"" + status.getValue() + "\", "
                + "\"createdAt\": \"" + createdAt.format(formatter) + "\", "
                + "\"updatedAt\": \"" + updatedAt.format(formatter) + "\""
                + "}";
    }


    public static Task fromJson(String json) {
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] parts = json.split(",");

        Map<String, String> map = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].strip(), keyValue[1].strip());
            }
        }

        int id = Integer.parseInt(map.get("id"));
        String description = map.get("description");
        Status status = Status.fromValue(map.get("status"));
        LocalDateTime createdAt = LocalDateTime.parse(map.get("createdAt"), formatter);
        LocalDateTime updatedAt = LocalDateTime.parse(map.get("updatedAt"), formatter);

        Task task = new Task(id, description);
        task.setStatus(status);
        task.createdAt = createdAt;
        task.updatedAt = updatedAt;

        return task;
    }

    @Override
    public String toString() {
        return "Task #" + id + " [" + status.getValue() + "] " + description
                + " (created: " + createdAt.format(formatter) + ", updated: " + updatedAt.format(formatter) + ")";
    }
}
