package ru.nsu.pivkin.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ru.nsu.pivkin.model.GameConfig;

/**
 * Обёртка над FXML-узлами.
 */
public class GameView {
    private final GameRenderer renderer;
    private final Label statusLabel;
    private final StackPane container;

    /**
     * Создаёт view из FXML-узлов, инжектированных контроллером.
     *
     * @param canvas - canvas для отрисовки
     * @param statusLabel - метка статуса внизу окна
     * @param container - контейнер canvas (для отслеживания ресайза)
     * @param onResize - callback, вызываемый при изменении размера контейнера
     * @param config - конфиг с цветами
     */
    public GameView(Canvas canvas, Label statusLabel, StackPane container, Runnable onResize, GameConfig config) {
        this.renderer = new GameRenderer(canvas, config);
        this.statusLabel = statusLabel;
        this.container = container;

        container.widthProperty().addListener((obs, oldVal, newVal) -> onResize.run());
        container.heightProperty().addListener((obs, oldVal, newVal) -> onResize.run());
    }

    /**
     * Возвращает рендерер для отрисовки игрового поля.
     *
     * @return - рендерер
     */
    public GameRenderer getRenderer() {
        return renderer;
    }

    /**
     * Подгоняет canvas под наименьшую сторону контейнера.
     */
    public void fitCanvas() {
        double size = Math.min(container.getWidth(), container.getHeight());
        renderer.resize(size);
    }

    /**
     * Задаёт текст статусной строки.
     *
     * @param text - отображаемый текст
     */
    public void setStatus(String text) {
        statusLabel.setText(text);
    }
}