package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class PrimeCheckerTest {
    private List<Integer> emptyList;
    private List<Integer> mixedNumbers;
    private List<Integer> allPrimes;
    private List<Integer> allNonPrimes;

    @BeforeEach
    void setUp() {
        emptyList = Arrays.asList();
        mixedNumbers = Arrays.asList(6, 8, 7, 13, 5, 9, 4);
        allPrimes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        allNonPrimes = Arrays.asList(4, 6, 8, 9, 10, 12, 14, 15, 16, 18);
    }


    //Sequential
    @Test
    void testSequentialEmptyList() {
        PrimeChecker checker = new SequentialChecker();
        assertFalse(checker.hasNonPrime(emptyList));
    }

    @Test
    void testSequentialMixedNumbers() {
        PrimeChecker checker = new SequentialChecker();
        assertTrue(checker.hasNonPrime(mixedNumbers));
    }

    @Test
    void testSequentialAllPrimes() {
        PrimeChecker checker = new SequentialChecker();
        assertFalse(checker.hasNonPrime(allPrimes));
    }

    @Test
    void testSequentialAllNonPrimes() {
        PrimeChecker checker = new SequentialChecker();
        assertTrue(checker.hasNonPrime(allNonPrimes));
    }


    //Thread
    @Test
    void testThreadCheckerEmptyList() {
        PrimeChecker checker = new ThreadChecker(4);
        assertFalse(checker.hasNonPrime(emptyList));
    }

    @Test
    void testThreadCheckerMixedNumbers() {
        PrimeChecker checker = new ThreadChecker(4);
        assertTrue(checker.hasNonPrime(mixedNumbers));
    }

    @Test
    void testThreadCheckerAllPrimes() {
        PrimeChecker checker = new ThreadChecker(4);
        assertFalse(checker.hasNonPrime(allPrimes));
    }

    @Test
    void testThreadCheckerAllNonPrimes() {
        PrimeChecker checker = new ThreadChecker(4);
        assertTrue(checker.hasNonPrime(allNonPrimes));
    }

    @Test
    void testThreadCheckerMoreThreadsThanElements() {
        List<Integer> smallList = Arrays.asList(2, 3, 5);
        PrimeChecker checker = new ThreadChecker(10);
        assertFalse(checker.hasNonPrime(smallList));
    }

    @Test
    void testThreadCheckerEarlyTermination() {
        List<Integer> numbers = Arrays.asList(1000003, 1000033, 4, 1000037, 1000039);
        PrimeChecker checker = new ThreadChecker(4);

        long startTime = System.nanoTime();
        boolean result = checker.hasNonPrime(numbers);
        long endTime = System.nanoTime();

        assertTrue(result);
        assertTrue(endTime - startTime < TimeUnit.SECONDS.toNanos(1));
    }


    //Parallel
    @Test
    void testParallelStreamEmptyList() {
        PrimeChecker checker = new ParallelChecker();
        assertFalse(checker.hasNonPrime(emptyList));
    }

    @Test
    void testParallelStreamMixedNumbers() {
        PrimeChecker checker = new ParallelChecker();
        assertTrue(checker.hasNonPrime(mixedNumbers));
    }

    @Test
    void testParallelStreamAllPrimes() {
        PrimeChecker checker = new ParallelChecker();
        assertFalse(checker.hasNonPrime(allPrimes));
    }

    @Test
    void testParallelStreamAllNonPrimes() {
        PrimeChecker checker = new ParallelChecker();
        assertTrue(checker.hasNonPrime(allNonPrimes));
    }


    //All
    @Test
    void testAllCheckersConsistency() {
        List<PrimeChecker> checkers = Arrays.asList(
                new SequentialChecker(),
                new ThreadChecker(2),
                new ThreadChecker(4),
                new ThreadChecker(8),
                new ParallelChecker()
        );

        List<List<Integer>> tests = Arrays.asList(
                emptyList,
                mixedNumbers,
                allPrimes,
                allNonPrimes,
                Arrays.asList(1000003, 1000033, 1000037, 1000039),
                Arrays.asList(1000000000, 999999937)
        );

        for (List<Integer> test : tests) {
            Boolean expected = checkers.get(0).hasNonPrime(test);
            for (PrimeChecker checker : checkers) {
                assertEquals(expected, checker.hasNonPrime(test),
                        "Checker " + checker.getName() + " gave different result for " + test);
            }
        }
    }
}
