package ru.nsu.pivkin.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.pivkin.model.GameConfig;
import ru.nsu.pivkin.model.Point;

import java.util.List;
import java.util.Set;

/**
 * Рисует состояние игры на Canvas.
 */
public class GameRenderer {
    private final Canvas canvas;
    private final GameConfig config;

    /**
     * Создаёт рендерер, привязанный к заданному canvas.
     *
     * @param canvas - canvas для отрисовки
     * @param config - конфиг с цветами
     */
    public GameRenderer(Canvas canvas, GameConfig config) {
        this.canvas = canvas;
        this.config = config;
    }

    /**
     * Перерисовывает поле целиком.
     *
     * @param cols - ширина поля в клетках
     * @param rows - высота поля в клетках
     * @param walls - позиции стен
     * @param food - позиции еды
     * @param body - тело змейки (голова — первый элемент)
     */
    public void render(int cols, int rows, Set<Point> walls, Set<Point> food, List<Point> body) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double cellW = canvas.getWidth() / cols;
        double cellH = canvas.getHeight() / rows;

        gc.setFill(Color.web(config.getColorBackground()));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.web(config.getColorWall()));
        for (Point p : walls) {
            gc.fillRect(p.getX() * cellW, p.getY() * cellH, cellW, cellH);
        }

        gc.setFill(Color.web(config.getColorFood()));
        for (Point p : food) {
            double m = cellW * 0.1;
            gc.fillOval(p.getX() * cellW + m, p.getY() * cellH + m, cellW - m * 2, cellH - m * 2);
        }

        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            double m = cellW * 0.1;
            gc.setFill(i == 0 ? Color.web(config.getColorSnakeHead()) : Color.web(config.getColorSnakeBody()));
            gc.fillOval(p.getX() * cellW + m, p.getY() * cellH + m, cellW - m * 2, cellH - m * 2);
        }
    }

    /**
     * Задаёт размер canvas.
     *
     * @param size - новый размер стороны (поле квадратное)
     */
    public void resize(double size) {
        canvas.setWidth(size);
        canvas.setHeight(size);
    }
}
