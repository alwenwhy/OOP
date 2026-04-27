package ru.nsu.pivkin;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int MEASUREMENTS = 100;
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        TestCase[] tests = {
                new TestCase("small_few", generateRandomNums(100, 100, 1)),
                new TestCase("small_many", generateRandomNums(10000, 100, 1)),
                new TestCase("large_few", generateRandomNums(100, 10000000, 1)),
                new TestCase("large_many", generateRandomNums(10000, 10000000, 1))
        };

        int[] threadCounts = {1, 2, 4, 8, 16, 32};

        for (TestCase test : tests) {
            List<String> results = new ArrayList<>();
            results.add("Type;Time_ns;Measurement");

            // Последовательное
            List<Long> seqTimes = new ArrayList<>();
            for (int m = 0; m < MEASUREMENTS; m++) {
                long time = check(new SequentialChecker(), test.numbers);
                seqTimes.add(time);
            }
            List<Long> seqFiltered = filter(seqTimes);
            for (int i = 0; i < seqFiltered.size(); i++) {
                results.add(String.format("Sequential;%d;%d", seqFiltered.get(i), i));
            }

            // Параллельное
            for (int threads : threadCounts) {
                List<Long> threadTimes = new ArrayList<>();
                for (int m = 0; m < MEASUREMENTS; m++) {
                    long time = check(new ThreadChecker(threads), test.numbers);
                    threadTimes.add(time);
                }
                List<Long> threadFiltered = filter(threadTimes);
                for (int i = 0; i < threadFiltered.size(); i++) {
                    results.add(String.format("Thread(%d);%d;%d", threads, threadFiltered.get(i), i));
                }
            }

            // ParallelStream
            List<Long> streamTimes = new ArrayList<>();
            for (int m = 0; m < MEASUREMENTS; m++) {
                long time = check(new ParallelChecker(), test.numbers);
                streamTimes.add(time);
            }
            List<Long> streamFiltered = filter(streamTimes);
            for (int i = 0; i < streamFiltered.size(); i++) {
                results.add(String.format("ParallelStream;%d;%d", streamFiltered.get(i), i));
            }

            save(results, test.name + ".csv");
        }
    }

    private static List<Integer> generateRandomNums(int count, int maxValue, double primePercent) {
        List<Integer> nums = new ArrayList<>();

        List<Integer> basePrimes = Arrays.asList(
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31,
                37, 41, 43, 47, 53, 59, 61, 67, 71, 73,
                79, 83, 89, 97, 101, 103, 107, 109, 113,
                127, 131, 137, 139, 149, 151, 157, 163,
                167, 173
        );

        List<Integer> baseComposites = Arrays.asList(
                4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20,
                21, 22, 24, 25, 26, 27, 28, 30, 32, 33,
                34, 35, 36, 38, 39, 40, 42, 44, 45, 46,
                48, 49, 50, 51, 52, 54, 55, 56, 57, 58,
                60, 62, 63, 64, 65, 66, 68, 69, 70, 72,
                74, 75, 76, 77, 78, 80, 81, 82, 84, 85,
                86, 87, 88, 90, 91, 92, 93, 94, 95, 96,
                98, 99, 100
        );

        for (int i = 0; i < count; i++) {
            boolean prime = random.nextDouble() < primePercent;
            int num;

            if (maxValue <= 100) {
                if (prime) {
                    num = basePrimes.get(random.nextInt(basePrimes.size()));
                } else {
                    num = baseComposites.get(random.nextInt(baseComposites.size()));
                }
            } else {
                if (prime) {
                    num = CheckerUtil.generateRandomPrime(maxValue);
                } else {
                    num = CheckerUtil.generateRandomComposite(maxValue);
                }
            }

            nums.add(num);
        }

        return nums;
    }

    private static List<Long> filter(List<Long> times) {
        List<Long> sorted = new ArrayList<>(times);
        java.util.Collections.sort(sorted);

        int trash = (int) Math.round(sorted.size() * 0.1);
        if (trash < 1) {
            trash = 1;
        }

        return sorted.subList(trash, sorted.size() - trash);
    }

    private static long check(PrimeChecker checker, List<Integer> numbers) {
        long start = System.nanoTime();
        checker.hasNonPrime(numbers);
        long end = System.nanoTime();
        return end - start;
    }

    private static void save(List<String> lines, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.println(line);
            }
            System.out.println("Результаты сохранены в " + filename);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения " + filename + ": " + e.getMessage());
        }
    }

    static class TestCase {
        String name;
        List<Integer> numbers;

        TestCase(String name, List<Integer> numbers) {
            this.name = name;
            this.numbers = numbers;
        }
    }
}