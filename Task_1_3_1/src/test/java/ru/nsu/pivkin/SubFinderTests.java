package ru.nsu.pivkin;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки класса SubFinder.
 */
public class SubFinderTests {
    @Test
    void testBasicExample() throws Exception {
        try (FileWriter writer = new FileWriter("test.txt", StandardCharsets.UTF_8)) {
            writer.write("абракадабра");
        }

        List<Integer> result = SubFinder.find("test.txt", "бра");
        assertEquals(Arrays.asList(1, 8), result);
    }

    @Test
    void testEmptyFile() throws Exception {
        try (FileWriter writer = new FileWriter("empty.txt")) {
            // Пустой файл
        }

        List<Integer> result = SubFinder.find("empty.txt", "test");
        assertTrue(result.isEmpty());
    }

    @Test
    void testDifferentBufferSizes() throws Exception {
        try (FileWriter writer = new FileWriter("test.txt", StandardCharsets.UTF_8)) {
            for (int i = 0; i < 10; i++) {
                writer.write("абракадабра");
            }
        }

        List<Integer> result1 = SubFinder.find("test.txt", "бра", 8); // 1 byte
        List<Integer> result2 = SubFinder.find("test.txt", "бра"); // default size - 1 KB
        List<Integer> result3 = SubFinder.find("test.txt", "бра", 8 * 1024); // 8 KB

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    @Test
    void testCatchingExceptions() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> SubFinder.find("test.txt", "бра", 1));
        assertThrows(RuntimeException.class, () -> SubFinder.find("notexist.txt", "бра"));
    }

    @Test
    void testProbablyBigSize() throws Exception {
        int filesize = 100000;
        try (FileWriter writer = new FileWriter("test.txt", StandardCharsets.UTF_8)) {
            for (int i = 0; i < filesize; i++) {
                writer.write("абракадабру");
            }
        }

        List<Integer> result = SubFinder.find("test.txt", "бра");
        assertEquals(filesize, result.size());
    }
}
