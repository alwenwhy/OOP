package ru.nsu.pivkin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки класса SubFinder.
 */
public class SubFinderTests {
    @TempDir
    Path tempDir;

    @Test
    void testBasicExample() throws Exception {
        List<Integer> result = SubFinder.find(
                new BufferedReader(new StringReader("абракадабра")), "бра");
        assertEquals(Arrays.asList(1, 8), result);
    }

    @Test
    void testEmptyFile() throws Exception {
        List<Integer> result = SubFinder.find(new BufferedReader(new StringReader("")), "test");
        assertTrue(result.isEmpty());
    }

    @Test
    void testDifferentBufferSizes() throws Exception {
        try (FileWriter writer = new FileWriter(tempDir+"/test1.txt", StandardCharsets.UTF_8)) {
            for (int i = 0; i < 10; i++) {
                writer.write("абракадабра");
            }
        }

        List<Integer> result1 = SubFinder.find(new BufferedReader(new FileReader(tempDir+"/test1.txt", StandardCharsets.UTF_8)), "бра", 8); // 1 byte
        List<Integer> result2 = SubFinder.find(new BufferedReader(new FileReader(tempDir+"/test1.txt", StandardCharsets.UTF_8)), "бра"); // default size - 1 KB
        List<Integer> result3 = SubFinder.find(new BufferedReader(new FileReader(tempDir+"/test1.txt", StandardCharsets.UTF_8)), "бра", 8 * 1024); // 8 KB

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    @Test
    void testCatchingExceptions() throws Exception {
        try (FileWriter writer = new FileWriter(tempDir+"/test2.txt", StandardCharsets.UTF_8)) {
            for (int i = 0; i < 10; i++) {
                writer.write("абракадабра");
            }
        }
        assertThrows(IllegalArgumentException.class, () -> SubFinder.find(new BufferedReader(new FileReader(tempDir+"/test2.txt", StandardCharsets.UTF_8)), "бра", 1));
    }

    @Test
    void testProbablyBigSize() throws Exception {
        int fileSize = 1000;
        try (FileWriter writer = new FileWriter(tempDir+"/test3.txt", StandardCharsets.UTF_8)) {
            for (int i = 0; i < fileSize; i++) {
                writer.write("абракадабру");
            }
        }

        List<Integer> result = SubFinder.find(new BufferedReader(new FileReader(tempDir+"/test3.txt", StandardCharsets.UTF_8)), "бра");
        assertEquals(fileSize, result.size());
    }

    @Test
    void testSurrogatePairs() throws Exception {
        String smile = "\uD83D\uDE00\uD83D\uDE00\uD83D\uDE00";
        List<Integer> result = SubFinder.find(new BufferedReader(new StringReader(smile)), "\uD83D\uDE00");
        System.out.println(smile);
        System.out.println("\uD83D\uDE00");
        assertEquals(List.of(0,1,2), result);
    }

}
