package ru.nsu.pivkin.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Загружает уровень из текстового файла.
 * '#' - стена.
 * '.' - пустая клетка.
 */
public class LevelLoader {
    /**
     * Читает файл уровня из ресурсов и возвращает множество клеток-стен.
     * Если файл не найден - возвращает пустое множество.
     *
     * @param filename - имя файла из папки levels/
     * @return - множество координат стен
     */
    public static Set<Point> load(String filename) {
        Set<Point> walls = new HashSet<>();

        String path = "/ru/nsu/pivkin/levels/" + filename;

        try (InputStream is = LevelLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Уровень не найден: " + path);
                return walls;
            }

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
            );

            int row = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == '#') {
                        walls.add(new Point(col, row));
                    }
                }
                row++;
            }

        } catch (Exception e) {
            System.err.println("Ошибка загрузки уровня: " + e.getMessage());
        }

        return walls;
    }
}
