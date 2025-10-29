module bd.edu.seu.onlinequiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens bd.edu.seu.onlinequiz to javafx.fxml;
    exports bd.edu.seu.onlinequiz;
    exports bd.edu.seu.onlinequiz.controllers;
    opens bd.edu.seu.onlinequiz.controllers to javafx.fxml;
    exports bd.edu.seu.onlinequiz.model;
    opens bd.edu.seu.onlinequiz.model to javafx.fxml;
}