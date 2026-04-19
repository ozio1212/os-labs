package lab1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataFactory {

    private Random random;

    public DataFactory() {
        random = new Random();
    }

    // Generowanie ciągu N procesów
    public List<Process> generateSequence(int n, int maxArrivalOffset) {
        List<Process> sequence = new ArrayList<>();
        int currentArrivalTime = 0;

        for(int i=1; i<=n; i++) {
            // generujemy nierównomierny rozkład dlugości fazy procesora
            // założenie że 75% procent procesów jest krótka(1-10 jednostek czasu),
            // a 25% długich (11-50 jednostek czasu)
            int burstTime;
            if(random.nextDouble() < 0.75){
                burstTime = random.nextInt(10) + 1;
            } else {
                burstTime = random.nextInt(40) + 11;
            }

            // Moment zgłoszenia przesuwam o niewielką losową wartość
            // Jeśli maxArrivalOffset jest małe w stosunku do średniego burstTime, szybko utowrzy się kolejka
            int arrivalOffset = random.nextInt(maxArrivalOffset + 1);
            currentArrivalTime += arrivalOffset;

            sequence.add(new Process(i, burstTime, currentArrivalTime));
        }

        return sequence;
    }

    // pomocnicza metodka aby kopiować listę i przekazywać ja do innych algorytmów
    public List<Process> copySequence(List<Process> sequence){
        List<Process> copy = new ArrayList<>();
        for(Process p : sequence){
            copy.add(new Process(p));
        }
        return copy;
    }
}
