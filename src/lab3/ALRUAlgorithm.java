package lab3;

import java.util.LinkedList;
import java.util.List;

public class ALRUAlgorithm implements PageReplacementAlgorithm{

    // klasa pomocnicza trzymająca numer strony i jej bit odwołania
    private static class Page {
        int id;
        boolean referenceBit;

        Page(int id) {
            this.id = id;
            this.referenceBit = true;
        }
    }

    @Override
    public String getName() {
        return "ALRU";
    }

    @Override
    public int runSimulation(List<Integer> referenceString, int numberOfFrames) {
        LinkedList<Page> framesQueue = new LinkedList<>();
        int pageFaults = 0;

        for (int currentPageId : referenceString) {
            boolean isInMemory = false;

            for (Page p : framesQueue) {
                if (p.id == currentPageId) {
                    p.referenceBit = true;
                    isInMemory = true;
                    break;
                }
            }

            if (!isInMemory) {
                pageFaults++;

                if (framesQueue.size() == numberOfFrames) {
                    while (true) {
                        Page oldestPage = framesQueue.removeFirst();

                        if (!oldestPage.referenceBit) {
                            break;
                        } else {
                            oldestPage.referenceBit = false;
                            framesQueue.addLast(oldestPage);
                        }
                    }
                }
                framesQueue.addLast(new Page(currentPageId));
            }
        }
        return pageFaults;
    }
}
