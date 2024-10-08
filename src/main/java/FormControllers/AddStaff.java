package FormControllers;

import Classes.Staff;
import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddStaff {

    @FXML
    private TextField CNIC;

    @FXML
    private DatePicker DOB;

    @FXML
    private TextField address;

    @FXML
    private TextField age;

    @FXML
    private TextField fatherName;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField name;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Text promptMsg;

    @FXML
    private RadioButton rFemaleBtn;

    @FXML
    private RadioButton rMaleBtn;

    @FXML
    private ComboBox<String> speciality;

    @FXML
    private JFXButton submitBtn;

    private String imagePath;
    private static int counter =4;
    ToggleGroup genderGroup;
    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public AddStaff() throws SQLException {
    }

    @FXML
    private void initialize() {

        genderGroup = new ToggleGroup();
        rMaleBtn.setToggleGroup(genderGroup);
        rFemaleBtn.setToggleGroup(genderGroup);

        try {
            Connection connection =  DBConnection.getConnection();
            String query = "SELECT name FROM Speciality";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String Speciality = resultSet.getString("name");
                speciality.getItems().add(Speciality);
            }
        }catch (SQLException e){}
        DOB.setEditable(false);
    }

    @FXML
    void insertImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.getPath();
            Image image = new Image(imagePath);
            imageView.setImage(image);
            System.out.println(imagePath);
        } else {
            promptMsg.setText("Kindly Fill the form properly");}
    }

    @FXML
    void submitInfo(ActionEvent event) {
        if(inputIsValid()){
            String Name = name.getText();
            String FatherName = fatherName.getText();
            String cnic = CNIC.getText();
            int Age = Integer.parseInt(age.getText());
            String Speciality = speciality.getValue();
            String Address = address.getText();
            String phnNum = phoneNumber.getText();
            RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();
            String Gender = selectedRadioButton.getText();
            LocalDate selectedDate = DOB.getValue();
            String dob = selectedDate.toString();
            double salary=0.0;

            Staff staff = new Staff(null, Name, FatherName, cnic, Age, Gender, Address, dob,
                    phnNum, salary, Speciality, imagePath);
//            DBConnection.AddDataToSQL(student, "Student");
            confirmation();
        }
    }

    private boolean inputIsValid(){
        if (isAnyFieldEmpty()){
            promptMsg.setText("Kindly Fill the form properly");
        } else if (!isNumericAge()) {
//            agePrompt.setText("Enter valid Age");
        }
        return !isAnyFieldEmpty() && isNumericAge();
    }
    private boolean isAnyFieldEmpty() {
        return     name.getText().isEmpty()
                || fatherName.getText().isEmpty()
                || address.getText().isEmpty()
                || CNIC.getText().isEmpty()
                || age.getText().isEmpty()
                || phoneNumber.getText().isEmpty()
                || DOB.getValue() == null
                || (!rMaleBtn.isSelected() && !rFemaleBtn.isSelected())
                || speciality.getSelectionModel().isEmpty()
                || imageView.getImage() == null;
    }
    private boolean isNumericAge() {
        String checked = age.getText();
        return checked.matches("\\d+");
    }
    private void confirmation(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Staff added successfully");
        alert.showAndWait();
        name.clear();
        fatherName.clear();
        CNIC.clear();
        age.clear();
        address.clear();
        phoneNumber.clear();
        DOB.setValue(null);
        genderGroup.selectToggle(null);
        speciality.getSelectionModel().clearSelection();
        imageView.setImage(null);
    }


}
