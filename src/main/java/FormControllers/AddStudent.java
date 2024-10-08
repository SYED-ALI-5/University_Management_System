package FormControllers;

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
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import Classes.*;

import java.util.Date;

public class AddStudent {

    @FXML
    private TextField name,fatherName,CNIC,address,age,phoneNumber;

    @FXML
    private DatePicker DOB;

    @FXML
    private ComboBox<String> department;

    @FXML
    private ImageView imageView;

    @FXML
    private RadioButton rFemaleBtn;

    @FXML
    private RadioButton rMaleBtn;

    @FXML
    private Text stdPromptMsg,agePrompt;

    @FXML
    private JFXButton submitBtn,insertBtn;

    private String imagePath;
    ToggleGroup genderGroup;
    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public AddStudent() throws SQLException {
    }


    @FXML
    private void initialize() {

        genderGroup = new ToggleGroup();
        rMaleBtn.setToggleGroup(genderGroup);
        rFemaleBtn.setToggleGroup(genderGroup);

        try {
            Connection connection =  DBConnection.getConnection();
            String query = "SELECT name FROM department";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String departmentName = resultSet.getString("name");
                department.getItems().add(departmentName);
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
            stdPromptMsg.setText("Kindly Fill the form properly");}
    }

    @FXML
    void submitInfo(ActionEvent event) throws SQLException, FileNotFoundException {
        if(inputIsValid()){
            RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();

            String Gender = selectedRadioButton.getText();

            LocalDate selectedDate = DOB.getValue();
            String Name = name.getText();
            String FatherName = fatherName.getText();
            String cnic = CNIC.getText();
            int Age = Integer.parseInt(age.getText());
            String Department = department.getValue();
            String Address = address.getText();
            String phnNum = phoneNumber.getText();
            String dob = selectedDate.toString();

            int fee = 0;

            try {
                String query = "SELECT * FROM department WHERE name=?";
                statement = connection.prepareStatement(query);
                statement.setString(1, Department);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    fee = resultSet.getInt("Fee");
                }
            } catch (SQLException e) {
            }
            Student student = new Student(null, Name, FatherName, cnic, Age, Gender,
                    Department, Address, dob, phnNum, imagePath, 1, fee);
            DBConnection.AddDataToSQL(student);
            confirmation();
        }
    }
    private boolean inputIsValid(){
        if (isAnyFieldEmpty()){
            stdPromptMsg.setText("Kindly Fill the form properly");
        } else if (!isNumericAge()) {
            agePrompt.setText("Enter valid Age");
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
                || department.getSelectionModel().isEmpty()
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
        alert.setContentText("Student added successfully");
        alert.showAndWait();
        name.clear();
        fatherName.clear();
        CNIC.clear();
        age.clear();
        address.clear();
        phoneNumber.clear();
        DOB.setValue(null);
        genderGroup.selectToggle(null);
        department.getSelectionModel().clearSelection();
        imageView.setImage(null);
    }

}