module org.study.worksjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    exports org.study.worksjavafx;

    opens org.study.worksjavafx to javafx.fxml;
    opens org.study.worksjavafx.entities to javafx.base;


}