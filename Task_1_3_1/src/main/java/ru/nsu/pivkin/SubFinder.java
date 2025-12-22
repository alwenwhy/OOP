package ru.nsu.pivkin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для поиска всех вхождений подстроки в текстовом файле.
 * Поддерживает работу с файлами, размер которых превышает объём оперативной памяти,
 * за счёт чтения файла частями заданного размера.
 */
public class SubFinder {
    /**
     * Находит все вхождения подстроки в файле с использованием указанного размера буфера.
     *
     * @param filename - путь к файлу.
     * @param substring - подстрока.
     * @param buffersize - размер буфера для чтения файла.
     * @return - список индексов всех вхождений.
     * @throws IllegalArgumentException - если размер буфера меньше длины подстроки.
     * @throws RuntimeException - если возникает ошибка при чтении файла.
     */
    public static List<Integer> find(String filename, String substring, Integer buffersize) {
        List <Integer> res = new ArrayList<>();

        if (buffersize < substring.length()) {
            throw new IllegalArgumentException("Buffer size can't be less than substring size.");
        }

        if (substring.isEmpty()) {
            return res;
        }

        try (BufferedReader reader = createReader(filename)) {
            StringBuilder buffer = new StringBuilder();
            char[] charBuffer = new char[buffersize];
            int pos = 0;

            int bytesRead = reader.read(charBuffer, 0, charBuffer.length);
            while (bytesRead != -1) {
                buffer.append(charBuffer, 0, bytesRead);

                String text = buffer.toString();
                int fromIndex = 0;
                int index = text.indexOf(substring, fromIndex);
                while (index != -1) {
                    if (!res.contains(pos + index)) {
                        res.add(pos + index);
                    }

                    fromIndex = index + 1;
                    index = text.indexOf(substring, fromIndex);
                }

                int keepChars = Math.max(0, buffer.length() - substring.length());
                buffer.delete(0, keepChars);
                pos += keepChars;

                bytesRead = reader.read(charBuffer, 0, charBuffer.length);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error with file reading.");
        }

        return res;
    }

    /**
     * Находит все вхождения подстроки в файле с использованием фиксированного размера буфера.
     *
     * @param filename - путь к файлу.
     * @param substring - подстрока.
     * @return - список индексов всех вхождений.
     * @throws IllegalArgumentException - если размер буфера меньше длины подстроки.
     * @throws RuntimeException - если возникает ошибка при чтении файла.
     */
    public static List<Integer> find(String filename, String substring) {
        return find(filename, substring, 1024); // 1 KB
    }

    /**
     * Создаёт BuffedReader для чтения файла в кодировке UTF-8.
     *
     * @param filename - путь к файлу.
     * @return - BufferReader для чтения.
     * @throws FileNotFoundException - если файл не найден.
     */
    private static BufferedReader createReader(String filename) throws FileNotFoundException {
        FileInputStream stream = new FileInputStream(filename);
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        return new BufferedReader(reader);
    }
}
