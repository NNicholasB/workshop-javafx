module org.study.worksjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.study.worksjavafx to javafx.fxml;
    exports org.study.worksjavafx;
}