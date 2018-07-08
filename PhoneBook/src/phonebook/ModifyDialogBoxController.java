package phonebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import phonebook.addon.*;

public class ModifyDialogBoxController implements Initializable
{
    @FXML
    private AnchorPane modifyContactPane;
    @FXML
    private TextField name;
    @FXML
    private TextField phone;
    @FXML
    private TextArea address;
    @FXML
    private TextField email;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    //Constructor
    private static String dataFilename,indexNameFilename,indexPhoneFilename;
    public ModifyDialogBoxController()
    {
        ModifyDialogBoxController.dataFilename = "./dataFile.txt";
        ModifyDialogBoxController.indexNameFilename = "./sortByNames.txt";
        ModifyDialogBoxController.indexPhoneFilename = "./sortByPhoneNo.txt";
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void save(ActionEvent event) {
        String Name  = name.getText();
        String Phone = phone.getText();
        String Email = email.getText();
        String Address = address.getText();
        boolean isPhone = true; //NOTE : Put Category
        
        if((isPhone)?validation.isPhoneNo(Phone):validation.isName(Name))
        {
            fileOperation.modifyRecord(Phone+";"+Name+";"+Address+";"+Email);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Record Modified!");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Modification Failed");
            alert.showAndWait();
        }
        //else NOTE : error Message "Error!!!\nEnter valid details"
        Stage stage = (Stage) modifyContactPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) modifyContactPane.getScene().getWindow();
        stage.close();
    }
    
}
