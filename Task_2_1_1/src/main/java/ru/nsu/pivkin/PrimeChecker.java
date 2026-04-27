package ru.nsu.pivkin;

import java.util.List;

public interface PrimeChecker {
    boolean hasNonPrime(List<Integer> numbers);
    String getName();
}