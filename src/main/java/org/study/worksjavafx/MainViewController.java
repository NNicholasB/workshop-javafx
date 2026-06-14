package org.study.worksjavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("Menu Seller");
    }

    @FXML
    public void onMenuItemDepartmentAction(){
        System.out.println("Menu Department");
    }

    @FXML
    public void onMenuItemAboutAction(){
        System.out.println("Menu About");
    }

    @Override
    public void initialize(URL uri, ResourceBundle rb) {

    }
}
