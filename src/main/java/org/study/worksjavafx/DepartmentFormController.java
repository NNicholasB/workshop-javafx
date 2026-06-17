package org.study.worksjavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.study.worksjavafx.util.Constraints;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    @FXML
    private TextField textFieldId;

    @FXML
    private TextField textFieldName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonCancel;

    @FXML
    public void onBtSaveAction(){
        System.out.println("Act btn save");
    }

    @FXML
    public void onBtCancelAction(){
        System.out.println("Act btn cancel");
    }

    @Override
    public void initialize(URL uri, ResourceBundle rb) {
        initializeNodes();

    }
    private void initializeNodes(){
        Constraints.setTextFieldIntereger(textFieldId);
        Constraints.setTextFieldMaxLength(textFieldName,30);
    }
}
