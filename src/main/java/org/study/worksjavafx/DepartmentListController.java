package org.study.worksjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.study.worksjavafx.entities.Department;
import org.study.worksjavafx.services.DepartmentService;
import org.study.worksjavafx.util.Alerts;
import org.study.worksjavafx.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    private DepartmentService service;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department,Integer> tableColumnId;

    @FXML
    private TableColumn<Department,String> tableColumnName;

    @FXML
    private Button btNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(ActionEvent ev){
        Stage parentStage= Utils.currentStage(ev);
        createDialogForm("/org/study/worksjavafx/DepartmentForm.fxml",parentStage);

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
    }

    private void createDialogForm(String absoluteName,Stage parentStage){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane=loader.load();

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
}
