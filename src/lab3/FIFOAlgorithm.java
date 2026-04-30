package lab3;

import java.util.*;

public class FIFOAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "FIFO";
    }

    @Override
    public int runSimulation(List<Integer> referenceString, int numberOfFrames) {
        // Zbiór do szybkiego sprawdzania, czy strona jest w pamięci
        Set<Integer> physicalMemory = new HashSet<>();
        // Kolejka do śledzenia, która strona weszła jako pierwsza
        Queue<Integer> memoryQueue = new LinkedList<>();

        int pageFaults = 0;

        for (int page : referenceString) {
            // Jeśli strony nie ma w pamięci fizycznej, mamy błąd strony
            if (!physicalMemory.contains(page)) {
                pageFaults++;

                // Jeśli pamięć jest pełna, usuwamy najstarszą stronę (FIFO)
                if (physicalMemory.size() == numberOfFrames) {
                    int oldestPage = memoryQueue.poll();
                    physicalMemory.remove(oldestPage);
                }

                // Dodajemy nową stronę do pamięci i na koniec kolejki
                physicalMemory.add(page);
                memoryQueue.add(page);
            }
            // Jeśli strona już jest w pamięci, w FIFO nic nie robimy (nie odświeżamy jej)
        }

        return pageFaults;
    }
}
