package ru.nsu.pivkin;

import java.util.List;

public class SequentialChecker implements PrimeChecker {

    @Override
    public boolean hasNonPrime(List<Integer> numbers) {
        for (Integer num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return "Sequential";
    }



    private boolean isPrime(int n) {
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
}
