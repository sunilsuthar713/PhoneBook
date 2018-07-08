package phonebook;

import com.jfoenix.controls.JFXButton;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import phonebook.addon.fileOperation;

public class PhoneBookUIController implements Initializable {
    
    ObservableList<Contacts> list = FXCollections.observableArrayList();

    @FXML
    private TextField searchText;
    @FXML
    private TableView<Contacts> displayTable;
    @FXML
    private TableColumn<Contacts, String> nameCol;
    @FXML
    private TableColumn<Contacts, String> phoneCol;
    @FXML
    private TableColumn<Contacts, String> addressCol;
    @FXML
    private TableColumn<Contacts, String> emailCol;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton modifyButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private MenuItem modifySelect;
    @FXML
    private MenuItem deleteSelect;

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
    private void addDialog(ActionEvent event) {
        loadWindow("/phonebook/AddDialogBox.fxml","Add");
    }

    @FXML
    private void modifyDialog(ActionEvent event) {
        loadWindow("/phonebook/ModifyDialogBox.fxml","Modify");
    }

    @FXML
    private void deleteDialog(ActionEvent event) {
        loadWindow("/phonebook/DeleteBox.fxml","Delete");
    }
    
    void loadWindow(String loc, String title) 
    {
        try 
        {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) 
        {
            Logger.getLogger(PhoneBookUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void searchContact(ActionEvent event) {
        
        try {
            RandomAccessFile indexFile;
            String content = searchText.getText();
            initCol();
            if(Pattern.compile("^[0-9]+").matcher(content).matches())
                indexFile = new RandomAccessFile("sortByPhoneNo.txt", "rw");
            else
                indexFile = new RandomAccessFile("sortByNames.txt", "rw");
            String line;
            
            while((line = indexFile.readLine())!=null)
            {
                if(line.split(";")[0].equals(content))
                {
                    int position = Integer.parseInt(line.split(";")[1]);
                    RandomAccessFile datafile = new RandomAccessFile("dataFile.txt", "rw");
                    String contactDetails;
                    datafile.seek(position);
                    contactDetails = datafile.readLine();
            
                    String DetailsContent[] = contactDetails.split(";");
                    list.add(new Contacts(DetailsContent[1], DetailsContent[0], DetailsContent[2], DetailsContent[3]));
                }
            }
            
            if(content.equals(""))
                list.clear();
            
            displayTable.getItems().setAll(list);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PhoneBookUIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PhoneBookUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Method used to configure the table view to display Contact details
    public void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    //Method to delete the selected record on right click
    @FXML
    private void deleteSelect(ActionEvent event) {
        Contacts deleteSelectedRecord = displayTable.getSelectionModel().getSelectedItem();
        System.out.println(deleteSelectedRecord.phone.getValue());
        
        fileOperation.DeleteRecords("", deleteSelectedRecord.phone.getValue(), true);
            
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Record Deleted!");
        alert.showAndWait();
    }

    //Class of the type Contacts
    //It is used to store objects of the type Contacts for processing operations
    public class Contacts {
        private final SimpleStringProperty name;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty address;
        private final SimpleStringProperty email;

        public Contacts(String name, String phone, String address, String email) {
            this.name = new SimpleStringProperty(name);
            this.phone = new SimpleStringProperty(phone);
            this.address = new SimpleStringProperty(address);
            this.email = new SimpleStringProperty(email);
        }
        public String getName() {
            return name.get();
        }
        public String getPhone() {
            return phone.get();
        }
        public String getAddress() {
            return address.get();
        }
        public String getEmail() {
            return email.get();
        }
    }   
}