package ru.nsu.pivkin;

import java.util.List;

public class SequentialChecker implements PrimeChecker {

    @Override
    public boolean hasNonPrime(List<Integer> numbers) {
        for (Integer num : numbers) {
            if (!CheckerUtil.isPrime(num)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return "Sequential";
    }
}
