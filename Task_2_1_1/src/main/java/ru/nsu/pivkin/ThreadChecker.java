package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadChecker implements PrimeChecker {
    private final int threads;

    public ThreadChecker(int threads) {
        this.threads = threads;
    }

    @Override
    public boolean hasNonPrime(List<Integer> numbers) {
        AtomicBoolean found = new AtomicBoolean(false);
        List<Thread> thr = new ArrayList<>();

        int size = (int) Math.ceil((double) numbers.size() / threads);

        for (int i = 0; i < threads; i++) {
            final int start = i * size;
            final int end = Math.min(start + size, numbers.size());

            if (start >= numbers.size()) {
                break;
            }

            Thread thread = new Thread(() -> {
                for (int j = start; j < end && !found.get(); j++) {
                    if (!isPrime(numbers.get(j))) {
                        found.set(true);
                        break;
                    }
                }
            });

            thr.add(thread);
            thread.start();
        }

        for (Thread thread : thr) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return true;
            }
        }

        return found.get();
    }

    @Override
    public String getName() {
        return "ThreadPool(" + threads + ")";
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
