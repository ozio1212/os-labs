package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RANDAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public int runSimulation(List<Integer> referenceString, int numberOfFrames) {
        List<Integer> frames = new ArrayList<>();
        Random random = new Random();
        int pageFaults = 0;

        for (int page : referenceString) {
            if (!frames.contains(page)) {
                pageFaults++;

                if (frames.size() == numberOfFrames) {
                    int victimIndex = random.nextInt(numberOfFrames);
                    frames.set(victimIndex, page);
                } else {
                    frames.add(page);
                }
            }
        }
        return pageFaults;
    }
}
