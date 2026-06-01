package ru.nsu.pivkin.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.pivkin.model.Point;

import java.util.List;
import java.util.Set;

/**
 * Рисует состояние игры на Canvas.
 */
public class GameRenderer {
    private final Canvas canvas;

    /**
     * Создаёт рендерер, привязанный к заданному canvas.
     *
     * @param canvas - canvas для отрисовки
     */
    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
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

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.rgb(214, 156, 47));
        for (Point p : walls) {
            gc.fillRect(p.getX() * cellW, p.getY() * cellH, cellW, cellH);
        }

        gc.setFill(Color.rgb(240, 62, 62));
        for (Point p : food) {
            double m = cellW * 0.1;
            gc.fillOval(p.getX() * cellW + m, p.getY() * cellH + m, cellW - m * 2, cellH - m * 2);
        }

        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            double m = cellW * 0.1;
            gc.setFill(i == 0 ? Color.rgb(8, 127, 91) : Color.rgb(32, 201, 151));
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
