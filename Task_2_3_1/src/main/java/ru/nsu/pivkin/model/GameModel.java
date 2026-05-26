package ru.nsu.pivkin.model;

import ru.nsu.pivkin.enums.Direction;
import ru.nsu.pivkin.enums.GameState;
import ru.nsu.pivkin.objs.Point;
import ru.nsu.pivkin.objs.Snake;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Логика игры.
 */
public class GameModel {
    private final int cols;
    private final int rows;
    private final int foodCount;
    private final int winLength;

    private final Snake snake;
    private final Set<Point> food = new HashSet<>();
    private final Set<Point> walls;
    private GameState state = GameState.PAUSED;

    private final Random random = new Random();

    /**
     * Создаёт модель игры с заданными параметрами и без стен.
     *
     * @param cols - ширина поля в клетках
     * @param rows - высота поля в клетках
     * @param foodCount - количество единиц еды на поле одновременно
     * @param winLength - длина змейки для победы
     */
    public GameModel(int cols, int rows, int foodCount, int winLength) {
        this(cols, rows, foodCount, winLength, new HashSet<>());
    }

    /**
     * Создаёт модель игры с заданными параметрами и стенами уровня.
     *
     * @param cols - ширина поля в клетках
     * @param rows - высота поля в клетках
     * @param foodCount - количество единиц еды на поле одновременно
     * @param winLength - длина змейки для победы
     * @param walls - множество клеток-стен
     */
    public GameModel(int cols, int rows, int foodCount, int winLength, Set<Point> walls) {
        this.cols = cols;
        this.rows = rows;
        this.foodCount = foodCount;
        this.winLength = winLength;
        this.walls = walls;

        snake = new Snake(new Point(cols / 2, rows / 2), Direction.RIGHT);

        for (int i = 0; i < foodCount; i++) {
            spawnFood();
        }
    }

    /**
     * Выполняет один тик игры: двигает змейку и обрабатывает столкновения.
     * Если игра уже завершена, ничего не делает.
     */
    public void tick() {
        if (state != GameState.RUNNING) {
            return;
        }

        Point newHead = snake.getHead().step(snake.getDirection());

        if (isOutOfBounds(newHead) || snake.contains(newHead) || walls.contains(newHead)) {
            state = GameState.LOSE;
            return;
        }

        if (food.contains(newHead)) {
            snake.grow();
            food.remove(newHead);
            spawnFood();
        }

        snake.move();

        if (snake.getLength() >= winLength) {
            state = GameState.WIN;
        }
    }

    /**
     * Задаёт новое направление змейки.
     *
     * @param direction - желаемое направление
     */
    public void setDirection(Direction direction) {
        snake.setDirection(direction);
    }

    /**
     * Возвращает змейку.
     *
     * @return - змейка
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Возвращает множество клеток с едой.
     *
     * @return - позиции еды
     */
    public Set<Point> getFood() {
        return food;
    }

    /**
     * Возвращает множество клеток-стен.
     *
     * @return - позиции стен
     */
    public Set<Point> getWalls() {
        return walls;
    }

    /**
     * Возвращает текущее состояние игры.
     *
     * @return - PAUSED/RUNNING/WIN/LOSE
     */
    public GameState getState() {
        return state;
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
     * Возвращает длину змейки для победы.
     *
     * @return - winLength
     */
    public int getWinLength() {
        return winLength;
    }



    private boolean isOutOfBounds(Point p) {
        return p.getX() < 0 || p.getX() >= cols || p.getY() < 0 || p.getY() >= rows;
    }

    private void spawnFood() {
        if (food.size() >= foodCount) {
            return;
        }

        Point candidate;
        int attempts = 0;

        do {
            candidate = new Point(random.nextInt(cols), random.nextInt(rows));
            attempts++;
        } while (
            (snake.contains(candidate) || food.contains(candidate) || walls.contains(candidate))
            && attempts < cols * rows
        );

        if (!snake.contains(candidate) && !food.contains(candidate) && !walls.contains(candidate)) {
            food.add(candidate);
        }
    }

    /**
     * Переключает между PAUSED и RUNNING. Игнорируется после WIN/LOSE.
     */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }
}
