package org.study.worksjavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.study.worksjavafx.entities.Department;
import org.study.worksjavafx.util.Constraints;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;

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

    public void updateFormData(){
        if (entity==null){
            throw new IllegalStateException("Entity was null");
        }
        textFieldId.setText(String.valueOf(entity.getId()));
        textFieldName.setText(entity.getName());
    }
    public void setDepartment(Department entity){
        this.entity=entity;
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
