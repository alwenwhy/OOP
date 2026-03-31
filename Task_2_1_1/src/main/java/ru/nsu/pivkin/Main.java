package ru.nsu.pivkin;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = generateTestData();

        List<String> results = new ArrayList<>();
        results.add("Type;Time_ns");

        // Последовательное
        long seqTime = test(new SequentialChecker(), numbers);
        results.add(String.format("Sequential;%d", seqTime));
        System.out.println("Sequential: " + seqTime + " ns");

        // Параллельное
        int[] threadCounts = {1, 2, 4, 8, 16, 32};
        for (int threads : threadCounts) {
            long time = test(new ThreadChecker(threads), numbers);
            results.add(String.format("Thread(%d);%d", threads, time));
            System.out.println("Thread(" + threads + "): " + time + " ns");
        }

        // ParallelStream
        long streamTime = test(new ParallelChecker(), numbers);
        results.add(String.format("ParallelStream;%d", streamTime));
        System.out.println("ParallelStream: " + streamTime + " ns");

        save(results);
    }



    private static List<Integer> generateTestData() {
        List<Integer> primes = Arrays.asList(
                10000019, 10000079, 10000103, 10000121, 10000139,
                20000003, 20000009, 20000017, 20000021, 20000027,
                30000001, 30000007, 30000017, 30000023, 30000031,
                40000009, 40000019, 40000031, 40000037, 40000039,
                50000021, 50000023, 50000033, 50000047, 50000051
        );

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            result.addAll(primes);
        }

        return result;
    }

    private static long test(PrimeChecker checker, List<Integer> numbers) {
        long start = System.nanoTime();
        checker.hasNonPrime(numbers);
        long end = System.nanoTime();

        return end - start;
    }

    private static void save(List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv"))) {
            for (String line : lines) {
                writer.println(line);
            }
            System.out.println("\nResults saved to results.csv");
        } catch (Exception e) {
            System.err.println("Error saving: " + e.getMessage());
        }
    }
}