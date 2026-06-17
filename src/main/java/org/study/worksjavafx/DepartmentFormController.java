package org.study.worksjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.study.worksjavafx.db.DbException;
import org.study.worksjavafx.entities.Department;
import org.study.worksjavafx.services.DepartmentService;
import org.study.worksjavafx.util.Alerts;
import org.study.worksjavafx.util.Constraints;
import org.study.worksjavafx.util.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;

    private DepartmentService service;


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
    public void onBtSaveAction(ActionEvent ev){
        if (entity==null){
            throw new IllegalStateException("Entity was null");
        }
        if (service ==null){
            throw new IllegalStateException("Service was null");
        }
        try{
            entity=getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(ev).close();
        }catch (DbException e){
            Alerts.showAlert("Error Saving Object",null,e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Department getFormData() {
        Department obj=new Department();
        obj.setId(Utils.tryParseToInt(textFieldId.getText()));
        obj.setName(textFieldName.getText());
        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent ev){
        Utils.currentStage(ev).close();
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

    public void setService(DepartmentService service) {
        this.service = service;
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
