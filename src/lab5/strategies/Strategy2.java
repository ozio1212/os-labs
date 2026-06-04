package lab5.strategies;

import lab5.model.Processor;
import lab5.model.Task;
import java.util.List;
import java.util.Random;

public class Strategy2 implements Strategy {
    private final double p;
    private int queryCount = 0;
    private int migrationCount = 0;
    private final Random random = new Random();

    public Strategy2(double p) {
        this.p = p;
    }

    @Override
    public void onNewTask(Task task, int processorIndex, List<Processor> processors) {
        Processor x = processors.get(processorIndex);
        if (x.getCurrentLoad() > p) {
            boolean sent = false;
            int maxAttempts = processors.size() * 5; 
            for (int i = 0; i < maxAttempts; i++) {
                int yIndex = random.nextInt(processors.size());
                queryCount++;
                if (processors.get(yIndex).getCurrentLoad() < p) {
                    processors.get(yIndex).addTask(task);
                    if (yIndex != processorIndex) {
                        migrationCount++;
                    }
                    sent = true;
                    break;
                }
            }
            if (!sent) {
                x.addTask(task);
            }
        } else {
            x.addTask(task);
        }
    }

    @Override
    public void onTick(List<Processor> processors) {
    }

    @Override
    public String getName() {
        return "Strategia 2 (p=" + p + ")";
    }

    @Override
    public int getQueryCount() {
        return queryCount;
    }

    @Override
    public int getMigrationCount() {
        return migrationCount;
    }

    @Override
    public void resetStats() {
        queryCount = 0;
        migrationCount = 0;
    }
}
