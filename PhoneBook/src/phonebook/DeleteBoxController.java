package phonebook;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import phonebook.addon.*;

public class DeleteBoxController implements Initializable {

    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane deleteContactPane;

    //Constructor
    private static String dataFilename,indexNameFilename,indexPhoneFilename;
    @FXML
    private TextField keyText;
    public DeleteBoxController()
    {
        DeleteBoxController.dataFilename = "./dataFile.txt";
        DeleteBoxController.indexNameFilename = "./sortByNames.txt";
        DeleteBoxController.indexPhoneFilename = "./sortByPhoneNo.txt";
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
    private void deleteContact(ActionEvent event) {
        
        String key = keyText.getText();
        String Name=null;
        String Phone=null;
        boolean isPhone;
        isPhone = Pattern.compile("^[0-9]+").matcher(key).matches();
        if(isPhone == true)
            Phone = key;
        else
            Name = key;
        
        if((isPhone)?validation.isPhoneNo(Phone):validation.isName(Name))
        {
            fileOperation.DeleteRecords(Name, Phone, isPhone);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Record Deleted!");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Deletion Failed");
            alert.showAndWait();
        }
        //else NOTE : error Message "Error!!!\nEnter valid details"
        Stage stage = (Stage) deleteContactPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) deleteContactPane.getScene().getWindow();
        stage.close();
    }
    
}
