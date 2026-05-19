package ru.nsu.pivkin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.pivkin.master.DistributedPrimeChecker;
import ru.nsu.pivkin.master.MasterServer;
import ru.nsu.pivkin.master.SlaveHandler;
import ru.nsu.pivkin.slave.SlaveClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты классов из master.
 * Каждый тест использует отдельный порт, чтобы избежать конфликтов.
 */
class DistributedPrimeCheckerTest {
    private static final int BASE_PORT = 15000;
    private static int portCounter = 0;

    private ExecutorService slaveExecutor;
    private MasterServer server;
    private int port;

    @BeforeEach
    void setUp() {
        port = BASE_PORT + (portCounter++);
        slaveExecutor = Executors.newCachedThreadPool();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        slaveExecutor.shutdownNow();
        slaveExecutor.awaitTermination(2, TimeUnit.SECONDS);
    }

    /**
     * Запускает N слейвов в фоновых потоках и ждёт подключения мастера.
     */
    private List<SlaveHandler> startSlaves(int slaveCount) throws Exception {
        for (int i = 0; i < slaveCount; i++) {
            final int p = port;
            slaveExecutor.submit(() -> new SlaveClient("localhost", p).start());
        }

        server = new MasterServer(port);
        server.waitForSlaves(slaveCount);
        return server.getSlaves();
    }

    private void stopWorkers(List<SlaveHandler> workers) throws Exception {
        for (SlaveHandler w : workers) {
            w.stopSlave();
        }
    }

    @Test
    @Timeout(10)
    void testReturnsTrueWhenArrayContainsNonPrime() throws Exception {
        List<SlaveHandler> workers = startSlaves(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        boolean result = checker.hasNonPrime(new int[]{6, 8, 7, 13, 5, 9, 4});

        assertTrue(result);
        stopWorkers(workers);
    }

    @Test
    @Timeout(15)
    void testReturnsTrueForTaskArrayWithComposite() throws Exception {
        int[] numbers = {
                20319251, 6997901, 6997927, 6997937,
                17858842, 6997967, 6998009, 6998029,
                6998039, 20165149, 6998051, 6998053
        };
        List<SlaveHandler> workers = startSlaves(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        boolean result = checker.hasNonPrime(numbers);

        assertTrue(result);
        stopWorkers(workers);
    }

    @Test
    @Timeout(15)
    void testReturnsFalseWhenAllPrimesLarge() throws Exception {
        int[] numbers = {
                6997901, 6997927, 6997937, 6997967,
                6998009, 6998029, 6998039, 6998051, 6998053
        };
        List<SlaveHandler> workers = startSlaves(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        boolean result = checker.hasNonPrime(numbers);

        assertFalse(result);
        stopWorkers(workers);
    }

    @Test
    @Timeout(10)
    void testWorksWithOneSlave() throws Exception {
        List<SlaveHandler> workers = startSlaves(1);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        assertTrue(checker.hasNonPrime(new int[]{4, 5, 7}));
        assertFalse(checker.hasNonPrime(new int[]{2, 3, 5}));

        stopWorkers(workers);
    }


    @Test
    @Timeout(10)
    void testReturnsTrueForSingleNonPrime() throws Exception {
        List<SlaveHandler> workers = startSlaves(1);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        assertTrue(checker.hasNonPrime(new int[]{4}));
        stopWorkers(workers);
    }

    @Test
    @Timeout(10)
    void testReturnsFalseForSinglePrime() throws Exception {
        List<SlaveHandler> workers = startSlaves(1);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        assertFalse(checker.hasNonPrime(new int[]{7}));
        stopWorkers(workers);
    }

    @Test
    @Timeout(10)
    void testReturnsTrueForArrayWithOne() throws Exception {
        List<SlaveHandler> workers = startSlaves(1);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        assertTrue(checker.hasNonPrime(new int[]{1}));
        stopWorkers(workers);
    }

    @Test
    @Timeout(10)
    void testReturnsTrueWhenNonPrimeIsFirst() throws Exception {
        List<SlaveHandler> workers = startSlaves(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        assertTrue(checker.hasNonPrime(new int[]{4, 3, 5, 7}));
        stopWorkers(workers);
    }

    @Test
    @Timeout(10)
    void testReturnsTrueWhenNonPrimeIsLast() throws Exception {
        List<SlaveHandler> workers = startSlaves(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers);

        assertTrue(checker.hasNonPrime(new int[]{3, 5, 7, 9}));
        stopWorkers(workers);
    }

    @Test
    @Timeout(15)
    void testCanBeCalledMultipleTimesOnSameSlaves() throws Exception {
        List<SlaveHandler> slaves = startSlaves(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(slaves);

        assertTrue(checker.hasNonPrime(new int[]{6, 7, 8}));
        assertFalse(checker.hasNonPrime(new int[]{2, 3, 5, 7}));
        assertTrue(checker.hasNonPrime(new int[]{9}));

        stopWorkers(slaves);
    }
}
