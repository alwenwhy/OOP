package ru.nsu.pivkin.model;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Конфигурация игры.
 */
public class GameConfig {
    private int cols = 20;
    private int rows = 20;
    private int foodCount = 3;
    private int winLength = 10;
    private long tickMs = 150;
    private String level = "default.txt";

    /**
     * Загружает конфиг из ресурса.
     *
     * @return - загруженный конфиг
     */
    public static GameConfig load() {
        GameConfig config = new GameConfig();

        try (InputStream is = GameConfig.class.getResourceAsStream("/ru/nsu/pivkin/config.json")) {
            if (is == null) {
                return config;
            }

            String json = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();

            config.cols = parseInt(json, "cols", config.cols);
            config.rows = parseInt(json, "rows", config.rows);
            config.foodCount = parseInt(json, "foodCount", config.foodCount);
            config.winLength = parseInt(json, "winLength", config.winLength);
            config.tickMs = parseInt(json, "tickMs", (int) config.tickMs);
            config.level = parseString(json, "level", config.level);

        } catch (Exception e) {
            System.err.println("Не удалось загрузить config.json: " + e.getMessage());
        }

        return config;
    }

    /**
     * Возвращает ширину поля.
     *
     * @return - количество столбцов
     */
    public int getCols() {
        return cols;
    }

    /**
     * Возвращает высоту поля.
     *
     * @return - количество строк
     */
    public int getRows() {
        return rows;
    }

    /**
     * Возвращает количество единиц еды одновременно.
     *
     * @return - foodCount
     */
    public int getFoodCount() {
        return foodCount;
    }

    /**
     * Возвращает длину змейки для победы.
     *
     * @return - winLength
     */
    public int getWinLength() {
        return winLength;
    }

    /**
     * Возвращает длительность одного тика в миллисекундах.
     *
     * @return - tickMs
     */
    public long getTickMs() {
        return tickMs;
    }

    /**
     * Возвращает имя файла уровня из папки levels/.
     *
     * @return - имя файла, например "default.txt"
     */
    public String getLevel() {
        return level;
    }



    private static int parseInt(String json, String key, int fallback) {
        String pattern = "\"" + key + "\"";

        int idx = json.indexOf(pattern);
        if (idx < 0) {
            return fallback;
        }

        int colon = json.indexOf(':', idx);
        if (colon < 0) {
            return fallback;
        }

        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        StringBuilder sb = new StringBuilder();
        while (start < json.length() && (Character.isDigit(json.charAt(start)) || json.charAt(start) == '-')) {
            sb.append(json.charAt(start++));
        }

        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static String parseString(String json, String key, String fallback) {
        String pattern = "\"" + key + "\"";

        int idx = json.indexOf(pattern);
        if (idx < 0) {
            return fallback;
        }

        int colon = json.indexOf(':', idx);
        if (colon < 0) {
            return fallback;
        }

        int open = json.indexOf('"', colon + 1);
        if (open < 0) {
            return fallback;
        }

        int close = json.indexOf('"', open + 1);
        if (close < 0) {
            return fallback;
        }

        return json.substring(open + 1, close);
    }
}
