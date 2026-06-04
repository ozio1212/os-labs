package lab4.model;

import lab3.ReferenceGenerator;

import java.util.ArrayList;
import java.util.List;

public class GlobalGenerator {

    public static class GlobalRequest {
        public final Process process;
        public final int page;

        public GlobalRequest(Process process, int page) {
            this.process = process;
            this.page = page;
        }
    }

    public List<Process> generateProcesses(int numberOfProcesses, int pagesPerProcess, int refsPerProcess) {
        List<Process> processes = new ArrayList<>();
        ReferenceGenerator localGen = new ReferenceGenerator();

        for (int i = 0; i < numberOfProcesses; i++) {
            int minPage = (i * pagesPerProcess) + 1;
            int maxPage = minPage + pagesPerProcess - 1;

            List<Integer> rawString = localGen.generate(refsPerProcess, pagesPerProcess, 50, 10);

            List<Integer> offsetString = new ArrayList<>();
            for (int p : rawString) {
                offsetString.add(p + minPage - 1);
            }

            processes.add(new Process(i, minPage, maxPage, offsetString));
        }
        return processes;
    }

    public List<GlobalRequest> generateGlobalString(List<Process> processes, int quantum) {
        List<GlobalRequest> globalString = new ArrayList<>();

        int[] progress = new int[processes.size()];
        boolean anyProcessActive = true;

        while (anyProcessActive) {
            anyProcessActive = false;

            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);
                List<Integer> pString = p.getFullReferenceString();

                int executedInThisQuantum = 0;
                while (executedInThisQuantum < quantum && progress[i] < pString.size()) {
                    globalString.add(new GlobalRequest(p, pString.get(progress[i])));
                    progress[i]++;
                    executedInThisQuantum++;
                    anyProcessActive = true;
                }
            }
        }
        return globalString;
    }
}