package ru.nsu.pivkin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.util.PrimeChecker;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для утилит
 */
class PrimeCheckerTest {
    @Test
    void testReturnsTrueАorSmallPrimes() {
        Assertions.assertTrue(PrimeChecker.isPrime(2));
        assertTrue(PrimeChecker.isPrime(3));
        assertTrue(PrimeChecker.isPrime(5));
        assertTrue(PrimeChecker.isPrime(7));
        assertTrue(PrimeChecker.isPrime(11));
        assertTrue(PrimeChecker.isPrime(13));
    }

    @Test
    void testReturnsFalseForOne() {
        assertFalse(PrimeChecker.isPrime(1));
    }

    @Test
    void testReturnsFalseForZero() {
        assertFalse(PrimeChecker.isPrime(0));
    }

    @Test
    void testReturnsFalseForNegatives() {
        assertFalse(PrimeChecker.isPrime(-1));
        assertFalse(PrimeChecker.isPrime(-7));
        assertFalse(PrimeChecker.isPrime(Integer.MIN_VALUE));
    }

    @Test
    void testReturnsFalseForEvenComposites() {
        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(6));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(100));
    }

    @Test
    void testReturnsFalseForOddComposites() {
        assertFalse(PrimeChecker.isPrime(9));
        assertFalse(PrimeChecker.isPrime(15));
        assertFalse(PrimeChecker.isPrime(25));
        assertFalse(PrimeChecker.isPrime(49));
    }

    @Test
    void testReturnsTrueForLargePrimes() {
        assertTrue(PrimeChecker.isPrime(6997901));
        assertTrue(PrimeChecker.isPrime(6997927));
        assertTrue(PrimeChecker.isPrime(6997937));
        assertTrue(PrimeChecker.isPrime(6997967));
        assertTrue(PrimeChecker.isPrime(6998009));
        assertTrue(PrimeChecker.isPrime(6998029));
        assertTrue(PrimeChecker.isPrime(6998039));
        assertTrue(PrimeChecker.isPrime(6998051));
        assertTrue(PrimeChecker.isPrime(6998053));
    }

    @Test
    void testReturnsTrueForPerfectSquareBoundary() {
        assertFalse(PrimeChecker.isPrime(49));
        assertTrue(PrimeChecker.isPrime(47));
    }

    @Test
    void testReturnsTrueWhenArrayContainsNonPrime() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{6, 8, 7, 13, 5, 9, 4}));
    }

    @Test
    void testReturnsFalseWhenAllPrimes() {
        assertFalse(PrimeChecker.hasNonPrime(new int[]{2, 3, 5, 7, 11, 13}));
    }

    @Test
    void testReturnsTrueWhenSingleNonPrime() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{4}));
    }

    @Test
    void testReturnsFalseWhenSinglePrime() {
        assertFalse(PrimeChecker.hasNonPrime(new int[]{7}));
    }

    @Test
    void testReturnsTrueWhenFirstElementIsNonPrime() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{1, 3, 5, 7}));
    }

    @Test
    void testReturnsTrueWhenLastElementIsNonPrime() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{3, 5, 7, 9}));
    }

    @Test
    void testReturnsTrueForEmptyLikeInput_withZero() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{0}));
    }
}
