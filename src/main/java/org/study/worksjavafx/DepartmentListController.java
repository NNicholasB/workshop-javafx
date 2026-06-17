package org.study.worksjavafx;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.study.worksjavafx.db.DbIntegrityException;
import org.study.worksjavafx.entities.Department;
import org.study.worksjavafx.listeners.DataChangeListener;
import org.study.worksjavafx.services.DepartmentService;
import org.study.worksjavafx.util.Alerts;
import org.study.worksjavafx.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.study.worksjavafx.util.Alerts.showConfirmation;

public class DepartmentListController implements Initializable, DataChangeListener {

    private DepartmentService service;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department,Integer> tableColumnId;

    @FXML
    private TableColumn<Department,String> tableColumnName;

    @FXML
    private TableColumn<Department,Department> tableColumnREMOVE;

    @FXML
    private TableColumn<Department,Department> tableColumnEDIT;

    @FXML
    private Button btNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(ActionEvent ev){
        Stage parentStage= Utils.currentStage(ev);
        Department obj=new Department();

        createDialogForm(obj,"/org/study/worksjavafx/DepartmentForm.fxml",parentStage);

    }

    public void setDepartmentService(DepartmentService service ){
        this.service=service;
    }


    @Override
    public void initialize(URL uri, ResourceBundle rb) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage= (Stage)Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service was null");
        }
        List<Department> list= service.findAll();
        obsList= FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Department obj,String absoluteName,Stage parentStage){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane=loader.load();

            DepartmentFormController controller=loader.getController();
            controller.setDepartment(obj);
            controller.setService(new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dioalogStage=new Stage();
            dioalogStage.setTitle("Enter Department data");
            dioalogStage.setScene(new Scene(pane));
            dioalogStage.setResizable(false);
            dioalogStage.initOwner(parentStage);
            dioalogStage.initModality(Modality.WINDOW_MODAL);
            dioalogStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlert("IO Exception","Error loadView",e.getMessage(), Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void initEditButtons(){
        tableColumnEDIT.setCellValueFactory(param->new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param->new TableCell<Department,Department>(){
            private final Button button=new Button("Edit");

            @Override
            protected void updateItem(Department obj,boolean empty){
                super.updateItem(obj,empty);
                if (obj == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(ev->createDialogForm(obj,"/org/study/worksjavafx/DepartmentForm.fxml",Utils.currentStage(ev)));
            };
        });}

    public void initRemoveButtons(){
        tableColumnREMOVE.setCellValueFactory(param->new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param->new TableCell<Department,Department>(){
            private final Button button=new Button("Remove");

            @Override
            protected void updateItem(Department obj,boolean empty){
                super.updateItem(obj, empty);
                if (obj == null){ setGraphic(null);
                    return;
            }
                setGraphic(button);
                button.setOnAction(ev->removeEntity(obj));
        };
    });
}
private void removeEntity(Department obj) {
       Optional<ButtonType> result =Alerts.showConfirmation("Confirmation","Are you sure to delete?",null, Alert.AlertType.CONFIRMATION);

       if (result.get() == ButtonType.OK){
           if (service == null){
               throw new IllegalStateException("Service was null");
           }
         try{
             service.remove(obj);
             updateTableView();
         }catch (DbIntegrityException e){
             Alerts.showAlert("Error removing Object",null,e.getMessage(), Alert.AlertType.ERROR);
         }
       }

   }


}