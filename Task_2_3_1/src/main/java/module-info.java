module ru.nsu.pivkin {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.nsu.pivkin to javafx.graphics;
    opens ru.nsu.pivkin.controller to javafx.fxml;
    opens ru.nsu.pivkin.view to javafx.fxml;
    opens ru.nsu.pivkin.model to javafx.base;
}
