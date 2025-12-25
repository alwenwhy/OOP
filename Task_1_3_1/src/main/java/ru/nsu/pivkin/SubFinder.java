package ru.nsu.pivkin;

import java.io.BufferedReader;
import java.io.IOException;
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
     * @param reader - buffered reader.
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
     * @param reader - bufferd reader.
     * @param substring - подстрока.
     * @param bufferSize - размер буфера для чтения файла.
     * @return - список индексов всех вхождений.
     * @throws IllegalArgumentException - если размер буфера меньше длины подстроки.
     * @throws RuntimeException - если возникает ошибка при чтении файла.
     */
    public static List<Integer> find(BufferedReader reader, String substring, Integer bufferSize) throws IOException {
        List<Integer> res = new ArrayList<>();

        if (bufferSize < substring.length()) {
            throw new IllegalArgumentException("Buffer size can't be less than substring size.");
        }

        if (substring.isEmpty()) {
            return res;
        }

        StringBuilder buffer = new StringBuilder();
        char[] charBuffer = new char[bufferSize];
        int posCodePoint = 0;
        int charsRead;

        while ((charsRead = reader.read(charBuffer)) != -1) {
            buffer.append(charBuffer, 0, charsRead);
            String text = buffer.toString();
            int fromIndex = 0;

            while (true) {
                int charIndex = text.indexOf(substring, fromIndex);
                if (charIndex == -1) {
                    break;
                }

                int indexCodePoint = posCodePoint + text.codePointCount(0, charIndex);
                if (!res.contains(indexCodePoint)) {
                    res.add(indexCodePoint);
                }

                fromIndex = charIndex + 1;
            }

            int keepChars = Math.max(0, buffer.length() - substring.length());
            int readedCodePoint = buffer.substring(0, keepChars).codePointCount(0, keepChars);

            buffer.delete(0, keepChars);
            posCodePoint += readedCodePoint;
        }

        return res;
    }

    private SubFinder() {
        throw new UnsupportedOperationException();
    }
}
