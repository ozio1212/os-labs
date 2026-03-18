package Lab1.model;

public class Process {
    private int id; // id procesu
    private int burstTime; // czas wykonania procesu
    private int arrivalTime; // moment zgloszenia
    private int waitingTime; // czas oczekiwania
    private int remainingTime; // przydatne pole do wywłaszczania (SJF)
    private int turnAroundTime; // czas przebywania w systemie (od zgłoszenia do zakoczenia)

    public Process(int id, int burstTime, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.remainingTime = burstTime;
        this.turnAroundTime = 0;
    }

    public Process(Process otherProcess){
        this.id = otherProcess.id;
        this.burstTime = otherProcess.burstTime;
        this.arrivalTime = otherProcess.arrivalTime;
        this.waitingTime = otherProcess.waitingTime;
        this.remainingTime = otherProcess.remainingTime;
        this.turnAroundTime = otherProcess.turnAroundTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public boolean isFinished() {
        return remainingTime <= 0;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnAroundTime = turnaroundTime;
    }
}
