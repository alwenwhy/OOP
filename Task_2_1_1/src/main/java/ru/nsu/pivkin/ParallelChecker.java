package ru.nsu.pivkin;

import java.util.List;

public class ParallelChecker implements PrimeChecker {

    @Override
    public boolean hasNonPrime(List<Integer> numbers) {
        return numbers.parallelStream()
                .anyMatch(n -> !isPrime(n));
    }

    @Override
    public String getName() {
        return "ParallelStream";
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
