package lab5.strategies;

import lab5.model.Processor;
import lab5.model.Task;
import java.util.List;

public interface Strategy {
    void onNewTask(Task task, int processorIndex, List<Processor> processors);
    void onTick(List<Processor> processors);
    
    String getName();
    int getQueryCount();
    int getMigrationCount();
    void resetStats();
}
