package lab3;

import java.util.LinkedList;
import java.util.List;

public class LRUAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "LRU";
    }

    @Override
    public int runSimulation(List<Integer> referenceString, int numberOfFrames) {
        LinkedList<Integer> frames = new LinkedList<>();
        int pageFaults = 0;

        for (int page : referenceString) {
            if (frames.contains(page)) {
                frames.remove(Integer.valueOf(page));
                frames.add(page);
            } else {
                pageFaults++;
                if (frames.size() == numberOfFrames) {
                    frames.removeFirst();
                }
                frames.add(page);
            }
        }
        return pageFaults;
    }
}
