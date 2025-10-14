import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = null;
    }


    public int getId() { return id; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }


    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\").replace("\"", "\"");
    }


    public String toJson() {
        String json = "{"
                + "\"id\": " + id + ", "
                + "\"description\": \"" + escapeJson(description.strip()) + "\", "
                + "\"status\": \"" + status.getValue() + "\", "
                + "\"createdAt\": \"" + createdAt.format(formatter) + "\", "
                + "\"updatedAt\": \"" + updatedAt.format(formatter) + "\"";

        if (deletedAt != null) {
            json += ", \"deletedAt\": \"" + deletedAt.format(formatter) + "\"";
        }

        return json + "}";
    }


    public static Task fromJson(String json) {
        // This is a slightly more robust manual parser.
        // It assumes a simple JSON object structure without nested objects or arrays.
        Map<String, String> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }

        List<String> parts = new ArrayList<>();
        int level = 0;
        int start = 0;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"') {
                level = 1 - level; // Simple quote toggle; doesn't handle escaped quotes
            } else if (c == ',' && level == 0) {
                parts.add(json.substring(start, i));
                start = i + 1;
            }
        }
        parts.add(json.substring(start));

        for (String part : parts) {
            String[] keyValue = part.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].replace("\"", "").strip();
                String value = keyValue[1].strip();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                map.put(key, value);
            }
        }

        int id = Integer.parseInt(map.get("id"));
        String description = map.get("description");
        Status status = Status.fromValue(map.get("status"));
        LocalDateTime createdAt = LocalDateTime.parse(map.get("createdAt"), formatter);
        LocalDateTime updatedAt = LocalDateTime.parse(map.get("updatedAt"), formatter);

        Task task = new Task(id, description);
        task.status = status;
        task.createdAt = createdAt;
        task.updatedAt = updatedAt;

        if (map.containsKey("deletedAt")) {
            task.deletedAt = LocalDateTime.parse(map.get("deletedAt"), formatter);
        }

        return task;
    }


    @Override
    public String toString() {
        String output = "Task #" + id + " [" + status.getValue() + "] " + description
                + " (created: " + createdAt.format(formatter) + ", updated: " + updatedAt.format(formatter);

        if (deletedAt != null) {
            output += ", deleted: " + deletedAt.format(formatter);
        }

        return output + ")";
    }
}