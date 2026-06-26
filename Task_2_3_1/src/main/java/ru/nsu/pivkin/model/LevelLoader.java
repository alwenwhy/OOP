package ru.nsu.pivkin.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Загружает уровень из конфига.
 * '#' - стена.
 */
public class LevelLoader {

    /**
     * Читает стены из строк, заданных в DSL-конфиге.
     *
     * @param rows - строки карты уровня
     * @return - множество координат стен
     */
    public static Set<Point> load(List<String> rows) {
        Set<Point> walls = new HashSet<>();

        for (int y = 0; y < rows.size(); y++) {
            String line = rows.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    walls.add(new Point(x, y));
                }
            }
        }

        return walls;
    }
}
