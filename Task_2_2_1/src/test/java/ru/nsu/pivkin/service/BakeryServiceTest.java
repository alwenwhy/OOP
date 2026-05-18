package ru.nsu.pivkin.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.pivkin.model.UnfinishedOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для основного сервиса пиццерии.
 */
class BakeryServiceTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void tearDown() {
        File serFile = new File("unfinished_orders.ser");
        if (serFile.exists()) {
            serFile.delete();
        }
    }

    private Path writeConfig(String content) throws IOException {
        Path configFile = tempDir.resolve("config.json");
        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            writer.write(content);
        }
        return configFile;
    }

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testStartWithValidConfig() throws IOException {
        String json = """
                {
                  "bakers": [
                    {"speed": 100, "name": "Baker1"}
                  ],
                  "couriers": [
                    {"capacity": 2, "name": "Courier1"}
                  ],
                  "storageCapacity": 3,
                  "workPeriod": 1,
                  "ordersPeriod": 2
                }
                """;
        Path configFile = writeConfig(json);

        BakeryService bakery = new BakeryService();
        assertDoesNotThrow(() -> bakery.start(configFile.toString()));
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testStartWithInvalidPath() {
        BakeryService bakery = new BakeryService();
        assertDoesNotThrow(() -> bakery.start("non_existent_config.json"));
    }

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testFullPipeline() throws IOException {
        String json = """
                {
                  "bakers": [
                    {"speed": 50, "name": "Baker1"},
                    {"speed": 80, "name": "Baker2"}
                  ],
                  "couriers": [
                    {"capacity": 3, "name": "Courier1"},
                    {"capacity": 2, "name": "Courier2"}
                  ],
                  "storageCapacity": 5,
                  "workPeriod": 1,
                  "ordersPeriod": 3
                }
                """;
        Path configFile = writeConfig(json);

        BakeryService bakery = new BakeryService();
        assertDoesNotThrow(() -> bakery.start(configFile.toString()));
    }

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testSmallStorageScenario() throws IOException {
        String json = """
                {
                  "bakers": [
                    {"speed": 30, "name": "Baker1"},
                    {"speed": 30, "name": "Baker2"}
                  ],
                  "couriers": [
                    {"capacity": 1, "name": "SlowCourier"}
                  ],
                  "storageCapacity": 1,
                  "workPeriod": 1,
                  "ordersPeriod": 5
                }
                """;
        Path configFile = writeConfig(json);

        BakeryService bakery = new BakeryService();
        assertDoesNotThrow(() -> bakery.start(configFile.toString()));
    }

    @Test
    @Timeout(value = 90, unit = TimeUnit.SECONDS)
    void testUnfinishedOrdersAreSerialized() throws IOException, ClassNotFoundException {
        String json = """
                {
                  "bakers": [
                    {"speed": 2000, "name": "SlowBaker"}
                  ],
                  "couriers": [
                    {"capacity": 1, "name": "Courier1"}
                  ],
                  "storageCapacity": 1,
                  "workPeriod": 1,
                  "ordersPeriod": 50
                }
                """;
        Path configFile = writeConfig(json);

        BakeryService bakery = new BakeryService();
        bakery.start(configFile.toString());

        File serFile = new File("unfinished_orders.ser");
        assertTrue(serFile.exists());

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
            @SuppressWarnings("unchecked")
            List<UnfinishedOrder> orders = (List<UnfinishedOrder>) ois.readObject();
            assertNotNull(orders);
            assertFalse(orders.isEmpty());
        }
    }
}