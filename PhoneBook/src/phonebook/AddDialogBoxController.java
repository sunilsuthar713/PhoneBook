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

public class AddDialogBoxController implements Initializable
{   
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
    @FXML
    private AnchorPane addContactPane;

    //Constructor
    private static String dataFilename,indexNameFilename,indexPhoneFilename;
    public AddDialogBoxController()
    {
        AddDialogBoxController.dataFilename = "./dataFile.txt";
        AddDialogBoxController.indexNameFilename = "./sortByNames.txt";
        AddDialogBoxController.indexPhoneFilename = "./sortByPhoneNo.txt";
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

    //Adds Records if valid entry only
    @FXML
    private void save(ActionEvent event) {
        String Name = name.getText();
        String Phone = phone.getText();
        String Address = address.getText();
        String Email = email.getText();

        if(validation.isName(Name)&&validation.isPhoneNo(Phone)&&validation.isEmail(Email))
        {
            long byteOffset = fileOperation.addRecord(dataFilename,Phone+";"+Name+";"+Address+";"+Email);
            System.out.println(byteOffset);
            fileOperation.addRecord(indexNameFilename,Name+";"+byteOffset);
            fileOperation.addRecord(indexPhoneFilename,Phone+";"+byteOffset);
            
            fileOperation.sortIndexFile(indexNameFilename);
            fileOperation.sortIndexFile(indexPhoneFilename);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Format");
            alert.showAndWait();
        }
        Stage stage = (Stage) addContactPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) addContactPane.getScene().getWindow();
        stage.close();
    }
    
}
