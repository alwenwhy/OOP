package ru.nsu.pivkin.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import ru.nsu.pivkin.enums.Direction;
import ru.nsu.pivkin.enums.GameState;
import ru.nsu.pivkin.model.GameConfig;
import ru.nsu.pivkin.model.GameModel;
import ru.nsu.pivkin.model.LevelLoader;
import ru.nsu.pivkin.objs.Point;

import java.util.List;

/**
 * "Мозг" игры. Обрабатывает ввод, запускает игровой цикл
 * и отрисовывает состояние модели на Canvas.
 */
public class GameController {
    @FXML private Canvas canvas;
    @FXML private StackPane canvasContainer;
    @FXML private Label statusLabel;

    private GameModel model;
    private GameConfig config;
    private AnimationTimer timer;
    private long lastTick = 0;

    /**
     * Загружает конфиг и запускает игру.
     */
    @FXML
    public void initialize() {
        config = GameConfig.load();
        startNewGame();
    }

    /**
     * Создаёт новую модель по текущему конфигу и запускает игровой цикл.
     * Игра начинается с паузы.
     */
    public void startNewGame() {
        lastTick = 0;
        model = new GameModel(
            config.getCols(),
            config.getRows(),
            config.getFoodCount(),
            config.getWinLength(),
            LevelLoader.load(config.getLevel())
        );

        if (timer != null) {
            timer.stop();
        }

        canvasContainer.widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        canvasContainer.heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        long tickNs = config.getTickMs() * 1_000_000L;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.getState() == GameState.RUNNING && now - lastTick >= tickNs) {
                    model.tick();
                    lastTick = now;
                }

                draw();
                updateStatus();
            }
        };

        timer.start();
    }

    /**
     * Обрабатывает нажатие клавиши.
     *
     * @param event - событие клавиатуры
     */
    public void handleKey(KeyEvent event) {
        switch (event.getCode()) {
            case R -> {
                if (model.getState() == GameState.WIN || model.getState() == GameState.LOSE) {
                    startNewGame();
                }
            }
            case E -> model.togglePause();
            case UP, W -> model.setDirection(Direction.UP);
            case DOWN, S -> model.setDirection(Direction.DOWN);
            case LEFT, A -> model.setDirection(Direction.LEFT);
            case RIGHT, D -> model.setDirection(Direction.RIGHT);
            default -> {}
        }
    }



    private void resizeCanvas() {
        double size = Math.min(canvasContainer.getWidth(), canvasContainer.getHeight());

        canvas.setWidth(size);
        canvas.setHeight(size);

        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int cols = model.getCols();
        int rows = model.getRows();

        double cellW = canvas.getWidth() / cols;
        double cellH = canvas.getHeight() / rows;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.rgb(214, 156, 47));
        for (Point p : model.getWalls()) {
            gc.fillRect(p.getX() * cellW, p.getY() * cellH, cellW, cellH);
        }

        gc.setFill(Color.rgb(240, 62, 62));
        for (Point p : model.getFood()) {
            double m = cellW * 0.1;
            gc.fillOval(p.getX() * cellW + m, p.getY() * cellH + m, cellW - m * 2, cellH - m * 2);
        }

        List<Point> body = model.getSnake().getBody();
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            double m = cellW * 0.1;

            gc.setFill(i == 0 ? Color.rgb(8, 127, 91) : Color.rgb(32, 201, 151));
            gc.fillOval(p.getX() * cellW + m, p.getY() * cellH + m, cellW - m * 2, cellH - m * 2);
        }
    }

    private void updateStatus() {
        String text;

        if (model.getState() == GameState.PAUSED) {
            text = "Пауза - нажмите E для продолжения";
        } else if (model.getState() == GameState.WIN) {
            text = "Победа! Нажмите R для новой игры";
        } else if (model.getState() == GameState.LOSE) {
            text = "Проигрыш! Нажмите R для новой игры";
        } else {
            text = "Длина: " + model.getSnake().getLength() + " / " + model.getWinLength();
        }

        statusLabel.setText(text);
    }
}
