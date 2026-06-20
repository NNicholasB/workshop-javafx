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
import org.study.worksjavafx.entities.Seller;
import org.study.worksjavafx.listeners.DataChangeListener;
import org.study.worksjavafx.services.DepartmentService;
import org.study.worksjavafx.services.SellerService;
import org.study.worksjavafx.util.Alerts;
import org.study.worksjavafx.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller,Integer> tableColumnId;

    @FXML
    private TableColumn<Seller,String> tableColumnName;

    @FXML
    private TableColumn<Seller,String> tableColumnEmail;

    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;

    @FXML
    private TableColumn<Seller,Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller,Seller> tableColumnREMOVE;

    @FXML
    private TableColumn<Seller,Seller> tableColumnEDIT;

    @FXML
    private Button btNew;

    private ObservableList<Seller> obsList;

    @FXML
    public void onBtNewAction(ActionEvent ev){
        Stage parentStage= Utils.currentStage(ev);
        Seller obj=new Seller();

        createDialogForm(obj,"/org/study/worksjavafx/SellerForm.fxml",parentStage);

    }

    public void setSellerService(SellerService service ){
        this.service=service;
    }


    @Override
    public void initialize(URL uri, ResourceBundle rb) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate,"dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
       Utils.formatTableColumnDouble(tableColumnBaseSalary,2);

        Stage stage= (Stage)Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service was null");
        }
        List<Seller> list= service.findAll();
        obsList= FXCollections.observableArrayList(list);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();

    }

    private void createDialogForm(Seller obj,String absoluteName,Stage parentStage){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane=loader.load();

            SellerFormController controller=loader.getController();
            controller.setSeller(obj);
            controller.setServices(new SellerService(),new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            controller.loadAssocietedObjects();
            Stage dioalogStage=new Stage();
            dioalogStage.setTitle("Enter Seller data");
            dioalogStage.setScene(new Scene(pane));
            dioalogStage.setResizable(false);
            dioalogStage.initOwner(parentStage);
            dioalogStage.initModality(Modality.WINDOW_MODAL);
            dioalogStage.showAndWait();

        }catch (IOException e){
            e.printStackTrace();
            Alerts.showAlert("IO Exception","Error loadView",e.getMessage(), Alert.AlertType.ERROR);

        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void initEditButtons(){
        tableColumnEDIT.setCellValueFactory(param->new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param->new TableCell<Seller,Seller>(){
            private final Button button=new Button("Edit");

            @Override
            protected void updateItem(Seller obj,boolean empty){
                super.updateItem(obj,empty);
                if (obj == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(ev->createDialogForm(obj,"/org/study/worksjavafx/SellerForm.fxml",Utils.currentStage(ev)));
            };
        });}

    public void initRemoveButtons(){
        tableColumnREMOVE.setCellValueFactory(param->new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param->new TableCell<Seller,Seller>(){
            private final Button button=new Button("Remove");

            @Override
            protected void updateItem(Seller obj,boolean empty){
                super.updateItem(obj, empty);
                if (obj == null){ setGraphic(null);
                    return;
            }
                setGraphic(button);
                button.setOnAction(ev->removeEntity(obj));
        };
    });
}
private void removeEntity(Seller obj) {
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