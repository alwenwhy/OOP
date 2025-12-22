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
public final class SubFinder {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * Находит все вхождения подстроки в файле с использованием фиксированного размера буфера.
     *
     * @param filename - путь к файлу.
     * @param substring - подстрока.
     * @return - список индексов всех вхождений.
     * @throws IllegalArgumentException - если размер буфера меньше длины подстроки.
     * @throws RuntimeException - если возникает ошибка при чтении файла.
     */
    public static List<Integer> find(BufferedReader reader, String substring) throws IOException {
        return find(reader, substring, DEFAULT_BUFFER_SIZE); // 1 KB
    }

    /**
     * Находит все вхождения подстроки в файле с использованием указанного размера буфера.
     *
     * @param filename - путь к файлу.
     * @param substring - подстрока.
     * @param bufferSize - размер буфера для чтения файла.
     * @return - список индексов всех вхождений.
     * @throws IllegalArgumentException - если размер буфера меньше длины подстроки.
     * @throws RuntimeException - если возникает ошибка при чтении файла.
     */
    public static List<Integer> find(BufferedReader reader, String substring, Integer bufferSize) throws IOException {
        List <Integer> res = new ArrayList<>();

        if (bufferSize < substring.length()) {
            throw new IllegalArgumentException("Buffer size can't be less than substring size.");
        }

        if (substring.isEmpty()) {
            return res;
        }

        StringBuilder buffer = new StringBuilder();
        char[] charBuffer = new char[bufferSize];
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

        return res;
    }

    private SubFinder() {
        throw new UnsupportedOperationException();
    }
}
