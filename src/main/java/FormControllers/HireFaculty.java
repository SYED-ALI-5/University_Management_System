package FormControllers;

import Classes.Faculty;
import Classes.Staff_Type;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HireFaculty {

    @FXML
    private TextField CNIC;

    @FXML
    private DatePicker DOB;

    @FXML
    private TextField address;

    @FXML
    private TextField age;

    @FXML
    private ComboBox<String> department;

    @FXML
    private ComboBox<String> scale;

    @FXML
    private ComboBox<String> type;

    @FXML
    private TextField fatherName;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField name,specialization;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Text promptMsg;

    @FXML
    private RadioButton rFemaleBtn;

    @FXML
    private RadioButton rMaleBtn;

    @FXML
    private JFXButton submitBtn;

    private String imagePath;
    ToggleGroup genderGroup;
    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement,statement2;
    ResultSet resultSet,resultSet2;

    public HireFaculty() throws SQLException {
    }


    @FXML
    private void initialize() {

        genderGroup = new ToggleGroup();
        rMaleBtn.setToggleGroup(genderGroup);
        rFemaleBtn.setToggleGroup(genderGroup);

        try {
            connection =  DBConnection.getConnection();
            String query = "SELECT name FROM department";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

//            String query2 = "SELECT scale FROM bps";
//            statement2 = connection.prepareStatement(query2);
//            resultSet2 = statement.executeQuery();


            while (resultSet.next()) {
                String departmentName = resultSet.getString("Name");
                department.getItems().add(departmentName);}
//            }while (resultSet2.next()) {
//                String Scale = resultSet.getString("Scale");
//                scale.getItems().add(Scale);
//            }
//
            type.getItems().addAll("PART_TIME", "FULL_TIME");
        }catch (SQLException e){}
        try {
            connection =  DBConnection.getConnection();
            String query2 = "SELECT Scales FROM bps";
            statement2 = connection.prepareStatement(query2);
            resultSet2 = statement2.executeQuery();

            while (resultSet2.next()) {
                String Scale = resultSet2.getString("Scales");
                scale.getItems().add(Scale);
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
    void submitInfo(ActionEvent event) throws SQLException, FileNotFoundException {
        if(inputIsValid()){
            String Name = name.getText();
            String FatherName = fatherName.getText();
            String cnic = CNIC.getText();
            int Age = Integer.parseInt(age.getText());
            String Department = department.getValue();
            String Address = address.getText();
            String phnNum = phoneNumber.getText();
            String Scale = scale.getValue();
            double salary=0.0;
            String Specialization = specialization.getText();
            Staff_Type Type = Staff_Type.valueOf(type.getValue());

            RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();
            String Gender = selectedRadioButton.getText();
            LocalDate selectedDate = DOB.getValue();
            String dob = selectedDate.toString();

            Faculty faculty = new Faculty(null,  Name,  FatherName,  cnic,  Age,  Gender, Specialization, Department,
                                            Address,  dob, phnNum,  Scale, salary, Type, imagePath);
            DBConnection.AddDataToSQL(faculty);
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
                || specialization.getText().isEmpty()
                || phoneNumber.getText().isEmpty()
                || DOB.getValue() == null
                || (!rMaleBtn.isSelected() && !rFemaleBtn.isSelected())
                || department.getSelectionModel().isEmpty()
                || type.getSelectionModel().isEmpty()
                || scale.getSelectionModel().isEmpty()
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
        alert.setContentText("Faculty added successfully");
        alert.showAndWait();
        name.clear();
        fatherName.clear();
        CNIC.clear();
        age.clear();
        address.clear();
        phoneNumber.clear();
        specialization.clear();
        DOB.setValue(null);
        genderGroup.selectToggle(null);
        department.getSelectionModel().clearSelection();
        scale.getSelectionModel().clearSelection();
        type.getSelectionModel().clearSelection();
        imageView.setImage(null);
    }

}
