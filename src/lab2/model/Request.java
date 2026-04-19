package lab2.model;

public class Request {
    private final int position;
    private final int arrivalTime;
    private final boolean isRealTime;
    private final int deadline; // Absolute deadline time
    private boolean finished;
    private int finishTime;

    public Request(int position, int arrivalTime) {
        this(position, arrivalTime, false, -1);
    }

    public Request(int position, int arrivalTime, boolean isRealTime, int deadline) {
        this.position = position;
        this.arrivalTime = arrivalTime;
        this.isRealTime = isRealTime;
        this.deadline = deadline;
        this.finished = false;
        this.finishTime = -1;
    }

    public int getPosition() {
        return position;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public boolean isRealTime() {
        return isRealTime;
    }

    public int getDeadline() {
        return deadline;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isDeadlineMissed() {
        return isRealTime && finishTime > deadline;
    }

    public Request copy() {
        Request copy = new Request(position, arrivalTime, isRealTime, deadline);
        copy.setFinished(this.finished);
        copy.setFinishTime(this.finishTime);
        return copy;
    }
}
