package lab3;

import java.util.List;

public interface PageReplacementAlgorithm {
    String getName();
    int runSimulation(List<Integer> referenceString, int numberOfFrames);
}
