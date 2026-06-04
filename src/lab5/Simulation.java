package lab5;

import lab5.model.Processor;
import lab5.model.Task;
import lab5.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    private final int N;
    private final int timeSteps;
    private final Strategy strategy;
    private final List<Processor> processors;
    private final double[] frequencies;
    private final Random random = new Random();
    private int nextTaskId = 0;

    public Simulation(int N, int timeSteps, Strategy strategy) {
        this.N = N;
        this.timeSteps = timeSteps;
        this.strategy = strategy;
        this.processors = new ArrayList<>();
        this.frequencies = new double[N];
        for (int i = 0; i < N; i++) {
            processors.add(new Processor(i));
            frequencies[i] = 0.1 + random.nextDouble() * 0.4;
        }
    }

    public Result run() {
        strategy.resetStats();
        double totalLoadSum = 0;
        List<Double> stepAverageLoads = new ArrayList<>();
        double[][] allLoads = new double[timeSteps][N];

        for (int t = 0; t < timeSteps; t++) {
            for (int i = 0; i < N; i++) {
                if (random.nextDouble() < frequencies[i]) {
                    double load = 0.02 + random.nextDouble() * 0.02;
                    int duration = 30 + random.nextInt(61);
                    Task task = new Task(nextTaskId++, load, duration);
                    strategy.onNewTask(task, i, processors);
                }
            }

            strategy.onTick(processors);

            double currentStepLoadSum = 0;
            for (int i = 0; i < N; i++) {
                Processor p = processors.get(i);
                double load = p.getCurrentLoad();
                allLoads[t][i] = load;
                currentStepLoadSum += load;
                p.tick();
            }
            double stepAvg = currentStepLoadSum / N;
            stepAverageLoads.add(stepAvg);
            totalLoadSum += stepAvg;
        }

        double averageLoad = totalLoadSum / timeSteps;
        
        double totalDeviationSum = 0;
        for (int t = 0; t < timeSteps; t++) {
            for (int i = 0; i < N; i++) {
                totalDeviationSum += Math.abs(allLoads[t][i] - averageLoad);
            }
        }
        double averageDeviation = totalDeviationSum / (timeSteps * N);

        return new Result(
                strategy.getName(),
                averageLoad,
                averageDeviation,
                strategy.getQueryCount(),
                strategy.getMigrationCount()
        );
    }

    public static class Result {
        public final String strategyName;
        public final double averageLoad;
        public final double averageDeviation;
        public final int queryCount;
        public final int migrationCount;

        public Result(String strategyName, double averageLoad, double averageDeviation, int queryCount, int migrationCount) {
            this.strategyName = strategyName;
            this.averageLoad = averageLoad;
            this.averageDeviation = averageDeviation;
            this.queryCount = queryCount;
            this.migrationCount = migrationCount;
        }
    }
}
