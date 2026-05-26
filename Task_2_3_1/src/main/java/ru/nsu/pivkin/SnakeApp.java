package ru.nsu.pivkin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.pivkin.controller.GameController;

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
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/ru/nsu/pivkin/game.fxml")
        );

        Scene scene = new Scene(loader.load(), 620, 660);

        GameController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKey);

        stage.setTitle("Змейка");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(300);
        stage.setMinHeight(300);

        stage.show();
    }
}
