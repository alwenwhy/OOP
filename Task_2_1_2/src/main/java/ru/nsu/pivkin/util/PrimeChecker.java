package ru.nsu.pivkin.util;

/**
 * Утилитарный класс для проверки простоты чисел.
 */
public class PrimeChecker {
    /**
     * Проверяет, является ли заданное целое число простым.
     *
     * @param n - проверяемое число
     * @return - true если число простое
     */
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Проверяет, содержит ли массив хотя бы одно составное число.
     *
     * @param nums - массив чисел для проверки
     * @return - true, если найдено составное число
     */
    public static boolean hasNonPrime(int[] nums) {
        for (int number : nums) {
            if (!isPrime(number)) {
                return true;
            }
        }

        return false;
    }
}
