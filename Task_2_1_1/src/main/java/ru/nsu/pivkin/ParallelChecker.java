package ru.nsu.pivkin;

import java.util.List;

public class ParallelChecker implements PrimeChecker {

    @Override
    public boolean hasNonPrime(List<Integer> numbers) {
        return numbers.parallelStream().anyMatch(n -> !CheckerUtil.isPrime(n));
    }

    @Override
    public String getName() {
        return "ParallelStream";
    }
}
