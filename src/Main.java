import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import service.TaskService;
import model.Status;
import model.Task;

public class Main {
    private static final TaskService taskService = new TaskService();

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
                        taskService.addTask(description);
                        System.out.println("[INFO] Task criada: \"" + description + "\"");
                    } else {
                        System.out.println("[ERROR] Informe uma descrição.");
                        System.out.println("Uso: task add <description>");
                    }
                    break;

                case "list":
                    System.out.println("--- Active Tasks ---");
                    printTaskTable(taskService.getActive(), false);
                    break;

                case "done":
                    if (args.length > 1) {
                        int id = Integer.parseInt(args[1]);
                        if (taskService.updateStatus(id, Status.DONE)) {
                            System.out.println("[INFO] Task #" + id + " marcada como DONE.");
                        } else {
                            System.out.println("[ERROR] Task #" + id + " não encontrada.");
                        }
                    } else {
                        System.out.println("[ERROR] Informe um ID.");
                        System.out.println("Uso: task done <id>");
                    }
                    break;

                case "update":
                    if (args.length > 2) {
                        int id = Integer.parseInt(args[1]);
                        Status status = Status.fromValue(args[2]);

                        if (taskService.updateStatus(id, status)) {
                            System.out.println("[INFO] Task #" + id + " atualizada para " + status.getValue());
                        } else {
                            System.out.println("[ERROR] Task #" + id + " não encontrada.");
                        }
                    } else {
                        System.out.println("[ERROR] Informe ID e status.");
                        System.out.println("Uso: task update <id> <todo|in_progress|done>");
                    }
                    break;

                case "delete":
                    if (args.length > 1) {
                        int id = Integer.parseInt(args[1]);

                        if (taskService.delete(id)) {
                            System.out.println("[INFO] Task #" + id + " movida para lixeira.");
                        } else {
                            System.out.println("[ERROR] Task #" + id + " não encontrada.");
                        }
                    } else {
                        System.out.println("[ERROR] Informe um ID.");
                        System.out.println("Uso: task delete <id>");
                    }
                    break;

                case "deleted":
                    System.out.println("--- Deleted Tasks ---");
                    printTaskTable(taskService.getDeleted(), true);
                    break;

                case "trash-clear":
                    taskService.emptyTrash();
                    System.out.println("[INFO] Lixeira esvaziada com sucesso.");
                    break;

                case "help":
                default:
                    printHelp();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] ID inválido. Use um número.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void printTaskTable(List<Task> tasks, boolean isDeletedList) {
        if (tasks.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateHeader = isDeletedList ? "Deleted At" : "Last Updated";

        System.out.printf("%-5s | %-12s | %-50s | %-25s%n", "ID", "Status", "Description", dateHeader);
        System.out.println(
                "------+--------------+----------------------------------------------------+--------------------------");

        for (Task task : tasks) {
            String description = task.getDescription();
            if (description.length() > 48) {
                description = description.substring(0, 45) + "...";
            }

            LocalDateTime date = isDeletedList ? task.getDeletedAt() : task.getUpdatedAt();
            if (date == null) {
                date = task.getUpdatedAt();
            }

            System.out.printf("%-5d | %-12s | %-50s | %-25s%n",
                    task.getId(),
                    task.getStatus().getValue(),
                    description,
                    date.format(formatter));
        }
    }

    private static void printHelp() {
        System.out.println("Task Tracker CLI");
        System.out.println("Uso: task <comando> [opções]");
        System.out.println();
        System.out.println("Comandos:");
        System.out.println("  add <description>    Adiciona uma nova task");
        System.out.println("  list                 Lista tasks ativas");
        System.out.println("  done <id>            Marca como DONE");
        System.out.println("  update <id> <status> Atualiza status(in_progress,done,todo)");
        System.out.println("  delete <id>          Move para lixeira");
        System.out.println("  deleted              Lista tasks deletadas");
        System.out.println("  trash-clear          Esvazia a lixeira");
        System.out.println("  help                 Mostra ajuda");
    }
}