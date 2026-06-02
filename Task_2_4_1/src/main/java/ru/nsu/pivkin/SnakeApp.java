package ru.nsu.pivkin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.pivkin.controller.GameController;
import ru.nsu.pivkin.model.GameConfig;
import ru.nsu.pivkin.model.GroovyLoader;

/**
 * Точка входа.
 */
public class SnakeApp extends Application {

    /**
     * Запуск приложения.
     *
     * @param args - аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GameConfig config = GroovyLoader.load();

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/ru/nsu/pivkin/game.fxml")
        );

        Scene scene = new Scene(loader.load(), config.getWindowWidth(), config.getWindowHeight());

        GameController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKey);

        stage.setTitle(config.getWindowTitle());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(config.getWindowMinWidth());
        stage.setMinHeight(config.getWindowMinHeight());

        stage.show();
    }
}
