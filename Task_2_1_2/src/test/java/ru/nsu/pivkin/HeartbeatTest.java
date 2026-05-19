package ru.nsu.pivkin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.pivkin.master.MasterServer;
import ru.nsu.pivkin.master.SlaveHandler;
import ru.nsu.pivkin.slave.SlaveClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для HEARTBEAT.
 */
class HeartbeatTest {
    private static final int BASE_PORT = 16000;
    private static int portCounter = 0;

    private ExecutorService slaveExecutor;
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

    private List<SlaveHandler> startCluster(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            final int p = port;
            slaveExecutor.submit(() -> new SlaveClient("localhost", p).start());
        }

        MasterServer server = new MasterServer(port);
        server.waitForSlaves(count);
        return server.getSlaves();
    }

    @Test
    @Timeout(10)
    void testHeartbeatReturnsTrue() throws Exception {
        List<SlaveHandler> slaves = startCluster(1);

        boolean alive = slaves.get(0).sendHeartbeat();

        assertTrue(alive);
        slaves.get(0).stopSlave();
    }

    @Test
    @Timeout(10)
    void testHeartbeatCanBeSentMultipleTimes() throws Exception {
        List<SlaveHandler> slaves = startCluster(1);
        SlaveHandler slave = slaves.get(0);

        assertTrue(slave.sendHeartbeat());
        assertTrue(slave.sendHeartbeat());
        assertTrue(slave.sendHeartbeat());

        slave.stopSlave();
    }

    @Test
    @Timeout(10)
    void testHeartbeatWorksForEachSlaveInCluster() throws Exception {
        List<SlaveHandler> slaves = startCluster(2);

        for (SlaveHandler slave : slaves) {
            assertTrue(slave.sendHeartbeat());
        }

        for (SlaveHandler slave : slaves) {
            slave.stopSlave();
        }
    }
}
