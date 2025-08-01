package TaskApp.impl;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import TaskApp.data.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import TaskApp.data.TaskStatus;

public class TaskManager {
    private static List<Task> tasks = new ArrayList<Task>();
    private static final String jsonFilePath = "tasks.json";

    public TaskManager() {
        loadTask();
    }

    public void loadTask() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(jsonFilePath)) {
            tasks = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            tasks = new ArrayList<>();
        }
    }

    public boolean saveTask() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            gson.toJson(tasks, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addTask(String description) {
        int id = tasks.isEmpty() ? 1 : tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
        LocalDateTime now = LocalDateTime.now();
        tasks.add(new Task(id, description, TaskStatus.TODO , now, now));
        if(saveTask()) {
            System.out.println("Task added successfully.");
        } else {
            System.out.println("Failed to save task.");
        }
    }

    public void updateTask(int id, String description) {
        Optional<Task> task_to_update = tasks.stream().filter(t -> t.getId() == id).findFirst();
        if(task_to_update.isPresent()) {
            Task task = task_to_update.get();
            task.setDescription(description);
            task.setUpdatedAt(LocalDateTime.now());
            if(saveTask()) {
                System.out.println("Task updated successfully.");
            } else {
                System.out.println("Failed to save task.");
            }
        } else {
            System.out.println("Task not found.");
        }
    }
    
    public void deletetask(int id) {
        Optional<Task> task_to_delete = tasks.stream().filter(t -> t.getId() == id).findFirst();
        if(task_to_delete.isPresent()) {
            if(tasks.remove(task_to_delete.get()) && saveTask()) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Failed to save task.");
            }
        } else {
            System.out.println("Task not found.");
        }
    }

    public void allTasks() {
        if(!tasks.isEmpty()) {
            System.out.println("---All Tasks---");
            tasks.forEach(System.out::println);
            System.out.println("---End of Tasks---");
        } else {
            System.out.println("No tasks available.");
        }
    }

    public void listTasksByStatus(TaskStatus status) {
        List<Task> filteredTasks = tasks.stream().filter(t -> t.getStatus() == status).toList();
        if(!filteredTasks.isEmpty()) {
            System.out.println("---Tasks with status: " + status + "---");
            filteredTasks.forEach(System.out::println);
            System.out.println("---End of Tasks---");
        } else {
            System.out.println("No tasks found with status: " + status);
        }
    }

    public void markTask(int id, TaskStatus status) {
        Optional<Task> task_to_mark = tasks.stream().filter(t -> t.getId() == id).findFirst();
        if(task_to_mark.isPresent()) {
            Task task = task_to_mark.get();
            task.setStatus(status);
            task.setUpdatedAt(LocalDateTime.now());
            if(saveTask()) {
                System.out.println("Task marked as " + status + " successfully.");
            } else {
                System.out.println("Failed to save task.");
            }
        } else {
            System.out.println("Task not found.");
        }
    }

   
}
