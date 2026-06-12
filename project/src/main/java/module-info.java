module org.study.project {
    requires javafx.controls;
    requires javafx.fxml;

    exports org.study.project.application;
    opens org.study.project.application to javafx.fxml;
}