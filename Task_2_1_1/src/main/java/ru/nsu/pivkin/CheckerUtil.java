package ru.nsu.pivkin;

import java.util.Random;

public final class CheckerUtil {
    private static final Random random = new Random(42);

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

    public static int generateRandomPrime(int maxValue) {
        while (true) {
            int n = random.nextInt(maxValue - 1000) + 1000;

            if (n % 2 == 0) {
                n++;
            }

            if (isPrime(n)) {
                return n;
            }
        }
    }

    public static int generateRandomComposite(int maxValue) {
        while (true) {
            int n = random.nextInt(maxValue - 10) + 10;

            if (!isPrime(n)) {
                return n;
            }
        }
    }
}
