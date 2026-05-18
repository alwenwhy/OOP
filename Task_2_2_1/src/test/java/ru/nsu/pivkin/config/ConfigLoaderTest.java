package ru.nsu.pivkin.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для загрузчика конфигурации.
 */
class ConfigLoaderTest {

    @TempDir
    Path tempDir;

    private Path writeConfig(String content) throws IOException {
        Path configFile = tempDir.resolve("config.json");
        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            writer.write(content);
        }
        return configFile;
    }

    @Test
    void testLoadValidConfig() throws IOException {
        String json = """
                {
                  "bakers": [
                    {"speed": 1000, "name": "Baker1"},
                    {"speed": 2000, "name": "Baker2"}
                  ],
                  "couriers": [
                    {"capacity": 3, "name": "Courier1"}
                  ],
                  "storageCapacity": 5,
                  "workPeriod": 10,
                  "ordersPeriod": 2
                }
                """;
        Path configFile = writeConfig(json);

        ConfigLoader loader = new ConfigLoader();
        ConfigLoader.BakeryConfig config = loader.load(configFile.toString());

        assertNotNull(config);
        assertEquals(2, config.bakers.size());
        assertEquals(1, config.couriers.size());
        assertEquals(5, config.storageCapacity);
        assertEquals(10, config.workPeriod);
        assertEquals(2, config.ordersPeriod);
    }

    @Test
    void testBakerConfig() throws IOException {
        String json = """
                {
                  "bakers": [
                    {"speed": 1500, "name": "Хаха"}
                  ],
                  "couriers": [],
                  "storageCapacity": 5,
                  "workPeriod": 10,
                  "ordersPeriod": 1
                }
                """;
        Path configFile = writeConfig(json);

        ConfigLoader loader = new ConfigLoader();
        ConfigLoader.BakeryConfig config = loader.load(configFile.toString());

        assertEquals(1, config.bakers.size());
        assertEquals("Хаха", config.bakers.get(0).name);
        assertEquals(1500, config.bakers.get(0).speed);
    }

    @Test
    void testCourierConfig() throws IOException {
        String json = """
                {
                  "bakers": [],
                  "couriers": [
                    {"capacity": 4, "name": "Жока"},
                    {"capacity": 2, "name": "Бока"}
                  ],
                  "storageCapacity": 10,
                  "workPeriod": 20,
                  "ordersPeriod": 1
                }
                """;
        Path configFile = writeConfig(json);

        ConfigLoader loader = new ConfigLoader();
        ConfigLoader.BakeryConfig config = loader.load(configFile.toString());

        assertEquals(2, config.couriers.size());
        assertEquals("Жока", config.couriers.get(0).name);
        assertEquals(4, config.couriers.get(0).capacity);
        assertEquals("Бока", config.couriers.get(1).name);
        assertEquals(2, config.couriers.get(1).capacity);
    }

    @Test
    void testLoadMissingFile() {
        ConfigLoader loader = new ConfigLoader();
        assertThrows(IOException.class, () -> loader.load("non_existent_file.json"));
    }

    @Test
    void testEmptyBakersAndCouriers() throws IOException {
        String json = """
                {
                  "bakers": [],
                  "couriers": [],
                  "storageCapacity": 1,
                  "workPeriod": 5,
                  "ordersPeriod": 1
                }
                """;
        Path configFile = writeConfig(json);

        ConfigLoader loader = new ConfigLoader();
        ConfigLoader.BakeryConfig config = loader.load(configFile.toString());

        assertTrue(config.bakers.isEmpty());
        assertTrue(config.couriers.isEmpty());
        assertEquals(1, config.storageCapacity);
    }
}