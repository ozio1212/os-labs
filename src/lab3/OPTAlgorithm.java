package lab3;

import java.util.ArrayList;
import java.util.List;

public class OPTAlgorithm implements PageReplacementAlgorithm{

    @Override
    public String getName() {
        return "OPT";
    }

    @Override
    public int runSimulation(List<Integer> referenceString, int numberOfFrames) {
        List<Integer> frames = new ArrayList<>();
        int pageFaults = 0;

        for (int i = 0; i < referenceString.size(); i++) {
            int currentPage = referenceString.get(i);

            if (!frames.contains(currentPage)) {
                pageFaults++;

                if (frames.size() == numberOfFrames) {
                    int victimIndex = findOptimalVictim(frames, referenceString, i + 1);
                    frames.set(victimIndex, currentPage);
                } else {
                    frames.add(currentPage);
                }
            }
        }
        return pageFaults;
    }

    private int findOptimalVictim(List<Integer> frames, List<Integer> referenceString, int currentIndex) {
        int farthestIndex = -1;
        int victimFrameIndex = -1;

        for (int i = 0; i < frames.size(); i++) {
            int framePage = frames.get(i);
            int nextOccurrence = referenceString.subList(currentIndex, referenceString.size()).indexOf(framePage);

            if (nextOccurrence == -1) {
                return i;
            }

            if (nextOccurrence > farthestIndex) {
                farthestIndex = nextOccurrence;
                victimFrameIndex = i;
            }
        }
        return victimFrameIndex;
    }
}
