package org.study.worksjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.study.worksjavafx.db.DbException;
import org.study.worksjavafx.entities.Department;
import org.study.worksjavafx.entities.Seller;
import org.study.worksjavafx.exceptions.ValidationException;
import org.study.worksjavafx.listeners.DataChangeListener;
import org.study.worksjavafx.services.DepartmentService;
import org.study.worksjavafx.services.SellerService;
import org.study.worksjavafx.util.Alerts;
import org.study.worksjavafx.util.Constraints;
import org.study.worksjavafx.util.Utils;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Callable;

public class SellerFormController implements Initializable {

    private Seller entity;

    private SellerService service;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners=new ArrayList<>();

    @FXML
    private TextField textFieldId;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private ComboBox<Department> comboBoxDepartment;


    @FXML
    private DatePicker datePickerBirthDate;

    @FXML
    private TextField textFieldBaseSalary;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonCancel;

    private ObservableList<Department> obsList;

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }
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
            notifyDataChangeListener();
            Utils.currentStage(ev).close();
        }
        catch (ValidationException e){
            setErrorsMessages(e.getErrors());
        }
        catch (DbException e){
            Alerts.showAlert("Error Saving Object",null,e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListener() {
        for (DataChangeListener listener: dataChangeListeners){
            listener.onDataChanged();
        }
    }


    private Seller getFormData() {
        Seller obj=new Seller();

        ValidationException exception=new ValidationException("Validation Error");

        obj.setId(Utils.tryParseToInt(textFieldId.getText()));
        if (textFieldName.getText() ==null || textFieldName.getText().trim().equals("")){
            exception.addError("name","Field can't be empty");
        }
        obj.setName(textFieldName.getText());
        if (exception.getErrors().size()>0){
            throw exception;
        }
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
        textFieldEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        textFieldBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
        if (entity.getBirthDate() != null){
            datePickerBirthDate.setValue(LocalDate.ofInstant( entity.getBirthDate().toInstant(), ZoneId.systemDefault()));

        }
       if (entity.getDepartment()==null){
           comboBoxDepartment.getSelectionModel().selectFirst();
       }
        comboBoxDepartment.setValue(entity.getDepartment());
    }


    public void setSeller(Seller entity){
        this.entity=entity;
    }

    public void setServices(SellerService service,DepartmentService departmentService) {
        this.service = service;
        this.departmentService=departmentService;
    }

    public void loadAssocietedObjects(){
        if (departmentService==null){
            throw new IllegalStateException("DepartmentService was null");
        }
        List<Department> list= departmentService.findAll();
        obsList= FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    @Override
    public void initialize(URL uri, ResourceBundle rb) {
        initializeNodes();

    }
    private void initializeNodes(){
        Constraints.setTextFieldIntereger(textFieldId);
        Constraints.setTextFieldMaxLength(textFieldName,70);
        Constraints.setTextFieldDouble(textFieldBaseSalary);
        Constraints.setTextFieldMaxLength(textFieldEmail,60);
        Utils.formatDatePicker(datePickerBirthDate,"dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    private void setErrorsMessages(Map<String,String> errors){
        Set<String> fields=errors.keySet();
        if (fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
    private void initializeComboBoxDepartment(){
        Callback<ListView<Department>,ListCell<Department>> factory= lv->new ListCell<Department>(){

            @Override
            protected void updateItem(Department item,boolean empty){
                super.updateItem(item,empty);
                setText(empty?"":item.getName());
            }
        };
    comboBoxDepartment.setCellFactory(factory);
    comboBoxDepartment.setButtonCell(factory.call(null));
    }}
