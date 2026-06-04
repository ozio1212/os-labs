package lab5.strategies;

import lab5.model.Processor;
import lab5.model.Task;
import java.util.List;
import java.util.Random;

public class Strategy1 implements Strategy {
    private final double p;
    private final int z;
    private int queryCount = 0;
    private int migrationCount = 0;
    private final Random random = new Random();

    public Strategy1(double p, int z) {
        this.p = p;
        this.z = z;
    }

    @Override
    public void onNewTask(Task task, int processorIndex, List<Processor> processors) {
        boolean sent = false;
        for (int i = 0; i < z; i++) {
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
            processors.get(processorIndex).addTask(task);
        }
    }

    @Override
    public void onTick(List<Processor> processors) {
    }

    @Override
    public String getName() {
        return "Strategia 1 (p=" + p + ", z=" + z + ")";
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
