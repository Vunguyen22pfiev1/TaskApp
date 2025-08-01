package TaskApp.app;

import TaskApp.data.TaskStatus;
import TaskApp.impl.TaskManager;

public class App {
    private static void printHelp() {
        System.out.println("\n--- Task cli usage ---");
        System.out.println("Add a new task:         task-cli add \"<description>\"");
        System.out.println("Update a task:          task-cli update <id> \"<new description>\"");
        System.out.println("Delete a task:          task-cli delete <id>");
        System.out.println("Mark task:              task-cli mark <status> <id>");
        System.out.println("List tasks:             task-cli list <status>");
        System.out.println("---------------------\n");
    }

    private static void executeCommand(String command, String args[]) {
        TaskManager taskManager = new TaskManager();
        try {
            switch (command) {
                case "add":
                    if(args.length < 2) {
                        System.out.println("Usage: task-cli add \"<description>\"");
                        return;
                    } else {
                        StringBuilder description = new StringBuilder();
                        for(int i = 1; i < args.length; i++) {
                            description.append(args[i]).append(" ");
                        }
                        taskManager.addTask(description.toString());
                        break;
                    }
                case "update":
                    if(args.length < 3) {
                        System.out.println("Usage: task cli update <id> \"<new description>\"");
                        return;
                    } else {
                        int id = Integer.parseInt(args[1]);
                        StringBuilder description = new StringBuilder();
                        for(int i = 2; i < args.length; i++) {
                            description.append(args[i]).append(" ");
                        }
                        taskManager.updateTask(id, description.toString());
                        break;
                    }
                case "delete":
                    if(args.length < 2) {
                        System.out.println("Usage: task cli delete <id>");
                        return;
                    } else {
                        int id = Integer.parseInt(args[1]);
                        taskManager.deletetask(id);
                        break;
                    }
                case "mark": 
                    if(args.length < 3) {
                        System.out.println("Usage: task cli mark <status> <id>");
                        return;
                    }
                    int id = Integer.parseInt(args[2]);
                    TaskStatus status = null;
                    switch(args[1].toLowerCase()) {
                        case "todo":
                            status = TaskStatus.TODO;
                            break;
                        case "in-progress":
                            status = TaskStatus.IN_PROGRESS;
                            break;
                        case "done":
                            status = TaskStatus.DONE;
                            break;
                        default:
                            System.out.println("Invalid status. Use todo, in-progress, or done.");
                            break;
                    }
                    if(status != null) {
                        taskManager.markTask(id, status);
                    }
                    break;
                    
                case "list":
                    if(args.length < 2) {
                        System.out.println("Usage: task cli list <status>");
                        return;
                    } else {
                        switch(args[1].toLowerCase()) {
                            case "todo":
                                taskManager.listTasksByStatus(TaskStatus.TODO);
                                break;
                            case "in-progress":
                                taskManager.listTasksByStatus(TaskStatus.IN_PROGRESS);
                                break;
                            case "done":
                                taskManager.listTasksByStatus(TaskStatus.DONE);
                                break;
                            case "all":
                                taskManager.allTasks();
                                break;
                            default:
                                System.out.println("Invalid status. Use todo, in-progress, done, or all.");
                                break;
                        }
                        break;
                    }    
                default:
                    System.out.println("Unknown command: " + command);
                    printHelp();
                    break;  
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {

        if(args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];
        executeCommand(command, args);
    }



    
}
