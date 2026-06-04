package lab5.model;

public class Task {
    private final int id;
    private final double cpuRequirement;
    private int remainingTime;

    public Task(int id, double cpuRequirement, int duration) {
        this.id = id;
        this.cpuRequirement = cpuRequirement;
        this.remainingTime = duration;
    }

    public void tick() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    public boolean isFinished() {
        return remainingTime <= 0;
    }

    public double getCpuRequirement() {
        return cpuRequirement;
    }

    public int getId() {
        return id;
    }
}
