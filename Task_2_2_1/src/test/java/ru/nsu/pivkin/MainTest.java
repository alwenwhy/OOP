package ru.nsu.pivkin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для главного класса.
 */
class MainTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void tearDown() {
        File serFile = new File("unfinished_orders.ser");
        if (serFile.exists()) {
            serFile.delete();
        }
    }

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testMainWithConfigArg() throws IOException {
        String json = """
                {
                  "bakers": [
                    {"speed": 50, "name": "Baker1"}
                  ],
                  "couriers": [
                    {"capacity": 1, "name": "Courier1"}
                  ],
                  "storageCapacity": 2,
                  "workPeriod": 1,
                  "ordersPeriod": 2
                }
                """;
        Path configFile = tempDir.resolve("config.json");
        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            writer.write(json);
        }

        assertDoesNotThrow(() -> Main.main(new String[]{configFile.toString()}));
    }
}
