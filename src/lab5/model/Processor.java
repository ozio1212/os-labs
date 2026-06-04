package lab5.model;

import java.util.ArrayList;
import java.util.List;

public class Processor {
    private final int id;
    private final List<Task> currentTasks;
    private double currentLoad;

    public Processor(int id) {
        this.id = id;
        this.currentTasks = new ArrayList<>();
        this.currentLoad = 0.0;
    }

    public void addTask(Task task) {
        currentTasks.add(task);
        currentLoad += task.getCpuRequirement();
    }

    public Task removeLastTask() {
        if (currentTasks.isEmpty()) return null;
        Task task = currentTasks.remove(currentTasks.size() - 1);
        currentLoad -= task.getCpuRequirement();
        return task;
    }

    public void tick() {
        List<Task> finishedTasks = new ArrayList<>();
        for (Task task : currentTasks) {
            task.tick();
            if (task.isFinished()) {
                finishedTasks.add(task);
            }
        }
        for (Task task : finishedTasks) {
            currentTasks.remove(task);
            currentLoad -= task.getCpuRequirement();
        }
        if (currentTasks.isEmpty()) {
            currentLoad = 0.0;
        }
    }

    public double getCurrentLoad() {
        return currentLoad;
    }

    public int getId() {
        return id;
    }
    
    public int getTaskCount() {
        return currentTasks.size();
    }

    public List<Task> getCurrentTasks() {
        return currentTasks;
    }
}
