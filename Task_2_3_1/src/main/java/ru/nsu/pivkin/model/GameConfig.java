package ru.nsu.pivkin.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Конфигурация игры.
 */
public class GameConfig {
    private int cols = 20;
    private int rows = 20;

    private long tickMs = 150;
    private int winLength = 10;

    private int foodCount = 3;

    private final Map<String, List<String>> levels = new LinkedHashMap<>();
    private String activeLevel = "default";

    private String colorBackground = "#000000";
    private String colorWall = "#D69C2F";
    private String colorFood = "#F03E3E";
    private String colorSnakeHead  = "#08715B";
    private String colorSnakeBody  = "#20C997";

    private String windowTitle = "Змейка";
    private int windowWidth = 620;
    private int windowHeight = 660;
    private int windowMinWidth = 300;
    private int windowMinHeight = 300;

    private String statusPaused  = "Пауза - нажмите E для продолжения";
    private String statusWin = "Победа! Нажмите R для новой игры";
    private String statusLose = "Проигрыш! Нажмите R для новой игры";
    private String statusRunning = "Длина: %d / %d";

    /**
     * Возвращает ширину поля.
     *
     * @return - количество столбцов
     */
    public int getCols() {
        return cols;
    }

    /**
     * Задаёт ширину поля.
     *
     * @param cols - количество столбцов
     */
    public void setCols(int cols) {
        this.cols = cols;
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
     * Задаёт высоту поля.
     *
     * @param rows - количество строк
     */
    public void setRows(int rows) {
        this.rows = rows;
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
     * Задаёт длительность тика.
     *
     * @param tickMs - миллисекунд на шаг
     */
    public void setTickMs(long tickMs) {
        this.tickMs = tickMs;
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
     * Задаёт длину змейки для победы.
     *
     * @param winLength - длина
     */
    public void setWinLength(int winLength) {
        this.winLength = winLength;
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
     * Задаёт количество единиц еды.
     *
     * @param foodCount - количество
     */
    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    /**
     * Добавляет строку в уровень с заданным именем.
     *
     * @param name - имя уровня
     * @param row - строка карты
     */
    public void addLevelRow(String name, String row) {
        levels.computeIfAbsent(name, k -> new ArrayList<>()).add(row);
    }

    /**
     * Возвращает строки карты активного уровня.
     *
     * @return - список строк карты или пустой список
     */
    public List<String> getActiveLevelRows() {
        return levels.getOrDefault(activeLevel, List.of());
    }

    /**
     * Задаёт имя активного уровня.
     *
     * @param name - имя уровня из DSL
     */
    public void setActiveLevel(String name) {
        this.activeLevel = name;
    }

    /**
     * Возвращает имя активного уровня.
     *
     * @return - имя уровня
     */
    public String getActiveLevel() {
        return activeLevel;
    }

    /**
     * Возвращает все загруженные уровни.
     *
     * @return - map имя -> строки карты
     */
    public Map<String, List<String>> getLevels() {
        return levels;
    }

    /**
     * @return - hex-цвет фона
     */
    public String getColorBackground() {
        return colorBackground;
    }

    /**
     * @param v - hex-цвет фона
     */
    public void setColorBackground(String v) {
        colorBackground = v;
    }

    /**
     * @return - hex-цвет стены
     */
    public String getColorWall() {
        return colorWall;
    }

    /**
     * @param v - hex-цвет стены
     */
    public void setColorWall(String v) {
        colorWall = v;
    }

    /**
     * @return - hex-цвет еды
     */
    public String getColorFood() {
        return colorFood;
    }

    /**
     * @param v - hex-цвет еды
     */
    public void setColorFood(String v) {
        colorFood = v;
    }

    /**
     * @return - hex-цвет головы змейки
     */
    public String getColorSnakeHead() {
        return colorSnakeHead;
    }

    /**
     * @param v - hex-цвет головы
     */
    public void setColorSnakeHead(String v) {
        colorSnakeHead = v;
    }

    /**
     * @return - hex-цвет тела змейки
     */
    public String getColorSnakeBody() {
        return colorSnakeBody;
    }

    /**
     * @param v - hex-цвет тела
     */
    public void setColorSnakeBody(String v) {
        colorSnakeBody = v;
    }

    /**
     * @return - заголовок окна
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * @param v - заголовок окна
     */
    public void setWindowTitle(String v) {
        windowTitle = v;
    }

    /**
     * @return - начальная ширина окна
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * @param v - ширина
     */
    public void setWindowWidth(int v) {
        windowWidth = v;
    }

    /**
     * @return - начальная высота окна
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * @param v - высота
     */
    public void setWindowHeight(int v) {
        windowHeight = v;
    }

    /**
     * @return - минимальная ширина окна
     */
    public int getWindowMinWidth() {
        return windowMinWidth;
    }

    /**
     * @param v - мин. ширина
     */
    public void setWindowMinWidth(int v) {
        windowMinWidth = v;
    }

    /**
     * @return - минимальная высота окна
     */
    public int getWindowMinHeight() {
        return windowMinHeight;
    }

    /**
     * @param v - мин. высота
     */
    public void setWindowMinHeight(int v) {
        windowMinHeight = v;
    }

    /**
     * @return - текст паузы
     */
    public String getStatusPaused() {
        return statusPaused;
    }

    /**
     * @param v - текст паузы
     */
    public void setStatusPaused(String v) {
        statusPaused = v;
    }

    /**
     * @return - текст победы
     */
    public String getStatusWin() {
        return statusWin;
    }

    /**
     * @param v - текст победы
     */
    public void setStatusWin(String v) {
        statusWin = v;
    }

    /**
     * @return - текст поражения
     */
    public String getStatusLose() {
        return statusLose;
    }

    /**
     * @param v - текст поражения
     */
    public void setStatusLose(String v) {
        statusLose = v;
    }

    /**
     * @return - шаблон текста во время игры (%d %d - длина и цель)
     */
    public String getStatusRunning() {
        return statusRunning;
    }

    /**
     * @param v - шаблон
     */
    public void setStatusRunning(String v) {
        statusRunning = v;
    }
}