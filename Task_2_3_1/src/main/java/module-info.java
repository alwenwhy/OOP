module ru.nsu.pivkin {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.nsu.pivkin to javafx.graphics;
    opens ru.nsu.pivkin.controller to javafx.fxml;
    opens ru.nsu.pivkin.model to javafx.base;
    opens ru.nsu.pivkin.enums to javafx.base;
    opens ru.nsu.pivkin.objs to javafx.base;
}
