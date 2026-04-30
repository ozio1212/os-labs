package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReferenceGenerator {

    public List<Integer> generate(int length, int virutalMemorySize, int phaseLength, int subsetSize) {
        List<Integer> referenceString = new ArrayList<>();
        Random rand = new Random();

        int referencesGenerated = 0;

        while (referencesGenerated < length) {
            List<Integer> currentSubset = new ArrayList<>();
            for (int i = 0; i < subsetSize; i++) {
                currentSubset.add(rand.nextInt(virutalMemorySize)+1);
            }

            int currentPhaseLength = Math.min(phaseLength, length-referencesGenerated);
            for (int i = 0; i < currentPhaseLength; i++) {
                if (rand.nextDouble() < 0.05){
                    referenceString.add(rand.nextInt(virutalMemorySize)+1);
                } else {
                    int randomIndex = rand.nextInt(currentSubset.size());
                    referenceString.add(currentSubset.get(randomIndex));
                }
                referencesGenerated++;
            }
        }
        return  referenceString;
    }
}
