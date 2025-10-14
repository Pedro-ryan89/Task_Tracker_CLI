import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "add":
                    if (args.length > 1) {
                        String description = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                        taskManager.addTask(description);
                        System.out.println("Task added: \"" + description + "\"");
                    } else {
                        System.out.println("Error: Please provide a description for the task.");
                        System.out.println("Usage: task add <description>");
                    }
                    break;
                case "list":
                    System.out.println("--- Active Tasks ---");
                    printTaskTable(taskManager.getActiveTasks(), false);
                    break;
                case "done":
                    if (args.length > 1) {
                        int id = Integer.parseInt(args[1]);
                        if (taskManager.updateTaskStatus(id, Status.DONE)) {
                            System.out.println("Task #" + id + " marked as DONE.");
                        } else {
                            System.out.println("Error: Task #" + id + " not found.");
                        }
                    } else {
                        System.out.println("Error: Please provide a task ID.");
                        System.out.println("Usage: task done <id>");
                    }
                    break;
                case "update":
                     if (args.length > 2) {
                        int id = Integer.parseInt(args[1]);
                        Status status = Status.fromValue(args[2]);
                        if (taskManager.updateTaskStatus(id, status)) {
                            System.out.println("Task #" + id + " updated to " + status.getValue() + ".");
                        } else {
                            System.out.println("Error: Task #" + id + " not found.");
                        }
                    } else {
                        System.out.println("Error: Please provide a task ID and a new status.");
                        System.out.println("Usage: task update <id> <todo|in_progress|done>");
                    }
                    break;
                case "delete":
                    if (args.length > 1) {
                        int id = Integer.parseInt(args[1]);
                        if (taskManager.deleteTask(id)) {
                            System.out.println("Task #" + id + " deleted.");
                        } else {
                            System.out.println("Error: Task #" + id + " not found.");
                        }
                    } else {
                        System.out.println("Error: Please provide a task ID.");
                        System.out.println("Usage: task delete <id>");
                    }
                    break;
                case "deleted":
                    System.out.println("--- Deleted Tasks ---");
                    printTaskTable(taskManager.getDeletedTasks(), true);
                    break;
                case "help":
                default:
                    printHelp();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid task ID provided. Please use a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printTaskTable(List<Task> tasks, boolean isDeletedList) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to display.");
            return;
        }

        DateTimeFormatter tableFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateHeader = isDeletedList ? "Deleted At" : "Last Updated";

        System.out.printf("%-5s | %-12s | %-50s | %-25s%n", "ID", "Status", "Description", dateHeader);
        System.out.println("------+--------------+----------------------------------------------------+--------------------------");

        for (Task task : tasks) {
            String description = task.getDescription();
            if (description.length() > 48) {
                description = description.substring(0, 45) + "...";
            }

            LocalDateTime displayDate = isDeletedList ? task.getDeletedAt() : task.getUpdatedAt();
            if (displayDate == null) {
                displayDate = task.getUpdatedAt();
            }

            System.out.printf("%-5d | %-12s | %-50s | %-25s%n",
                    task.getId(),
                    task.getStatus().getValue(),
                    description,
                    displayDate.format(tableFormatter));
        }
    }

    private static void printHelp() {
        System.out.println("Task Tracker CLI");
        System.out.println("Usage: task <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  add <description>    Add a new task.");
        System.out.println("  list                 List all active tasks.");
        System.out.println("  done <id>            Mark a task as done.");
        System.out.println("  update <id> <status> Update a task status (todo, in_progress, done).");
        System.out.println("  delete <id>          Move a task to the trash.");
        System.out.println("  deleted              List tasks in the trash.");
        System.out.println("  help                 Show this help message.");
    }
}
