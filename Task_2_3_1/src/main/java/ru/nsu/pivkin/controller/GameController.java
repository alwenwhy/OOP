package ru.nsu.pivkin.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import ru.nsu.pivkin.model.Direction;
import ru.nsu.pivkin.model.GameConfig;
import ru.nsu.pivkin.model.GameModel;
import ru.nsu.pivkin.model.GameState;
import ru.nsu.pivkin.GroovyLoader;
import ru.nsu.pivkin.model.LevelLoader;
import ru.nsu.pivkin.view.GameView;

/**
 * Связывает модель и view.
 */
public class GameController {
    @FXML private Canvas canvas;
    @FXML private StackPane canvasContainer;
    @FXML private Label statusLabel;

    private GameModel model;
    private GameConfig config;
    private GameView view;
    private AnimationTimer timer;
    private long lastTick = 0;

    /**
     * Загружает конфиг через Groovy DSL, создаёт view и запускает игру.
     */
    @FXML
    public void initialize() {
        config = GroovyLoader.load();
        view = new GameView(canvas, statusLabel, canvasContainer, this::onResize, config);
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
            LevelLoader.load(config.getActiveLevelRows())
        );

        if (timer != null) {
            timer.stop();
        }

        long tickNs = config.getTickMs() * 1_000_000L;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.getState() == GameState.RUNNING && now - lastTick >= tickNs) {
                    model.tick();
                    lastTick = now;
                }

                render();
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

    private void onResize() {
        view.fitCanvas();
        render();
    }

    private void render() {
        view.getRenderer().render(
            model.getCols(),
            model.getRows(),
            model.getWalls(),
            model.getFood(),
            model.getSnake().getBody()
        );
    }

    private void updateStatus() {
        String text;

        if (model.getState() == GameState.PAUSED) {
            text = config.getStatusPaused();
        } else if (model.getState() == GameState.WIN) {
            text = config.getStatusWin();
        } else if (model.getState() == GameState.LOSE) {
            text = config.getStatusLose();
        } else {
            text = String.format(config.getStatusRunning(), model.getSnake().getLength(), model.getWinLength());
        }

        view.setStatus(text);
    }
}
