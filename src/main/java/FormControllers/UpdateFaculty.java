package FormControllers;

import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateFaculty {

    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement,statement2,statement3,statement4;
    ResultSet resultSet,resultSet2,resultSet3,resultSet4;


    @FXML
    private JFXButton saveInfo,insertBtn;

    @FXML
    private TextField specialization;

    @FXML
    private TextField updatedAddress;

    @FXML
    private TextField updatedAge;

    @FXML
    private TextField updatedCNIC;

    @FXML
    private DatePicker updatedDOB;

    @FXML
    private ComboBox<String> updatedDepartment;

    @FXML
    private ComboBox<String> addCourse;

    @FXML
    private ComboBox<String> updatedScale;

    @FXML
    private ComboBox<String> updatedType;

    @FXML
    private TextField updatedFatherName;

    @FXML
    private RadioButton updatedFemaleBtn;

    @FXML
    private ImageView updatedImageView;

    @FXML
    private RadioButton updatedMaleBtn;

    @FXML
    private TextField updatedName;

    @FXML
    private TextField updatedPhnNum;

    @FXML
    private Text updatedPromptMsg;

    @FXML
    private TextField updatedSearchFID;

    Blob Image;
    FileInputStream fis;
    ToggleGroup genderGroup;
    String imagePath;

    public UpdateFaculty() throws SQLException {
    }

    @FXML
    private void initialize() {

        genderGroup = new ToggleGroup();
        updatedFemaleBtn.setToggleGroup(genderGroup);
        updatedMaleBtn.setToggleGroup(genderGroup);
        updatedDOB.setEditable(false);
        try {
            connection =  DBConnection.getConnection();
            String query = "SELECT Name FROM department";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            String query2 = "SELECT Scales FROM bps";
            statement2 = connection.prepareStatement(query2);
            resultSet2 = statement2.executeQuery();

            while (resultSet.next()) {
                String departmentName = resultSet.getString("Name");
                updatedDepartment.getItems().add(departmentName);
            }while (resultSet2.next()) {
                String Scale = resultSet2.getString("Scales");
                updatedScale.getItems().add(Scale);
            }

            List<String> types = new ArrayList<>();
            types.add("PART_TIME");
            types.add("FULL_TIME");
            updatedType.getItems().addAll(types);

        }catch (SQLException e){}
    }

    @FXML
    void UpdateSearch(MouseEvent event) throws SQLException {

        String query3 = "SELECT Department FROM Faculty WHERE ID =?";
        statement3 = connection.prepareStatement(query3);
        statement3.setString(1,updatedSearchFID.getText());
        resultSet3 = statement3.executeQuery();
        String depart =null;
        while (resultSet3.next()) {
            depart = resultSet3.getString("Department");
        }
        String query4 = "SELECT Title FROM Courses WHERE Department =?";
        statement4 = connection.prepareStatement(query4);
        statement4.setString(1, depart);
        resultSet4 = statement4.executeQuery();
        while (resultSet4.next()) {
            String courses = resultSet4.getString("Title");
            addCourse.getItems().add(courses);
        }

        try {
            String query = "SELECT * FROM faculty WHERE ID=?";
            statement = connection.prepareStatement(query);
            statement.setString(1,updatedSearchFID.getText());
            resultSet = statement.executeQuery();
            boolean chk=true;

            while (resultSet.next()) {
                updatedPromptMsg.setText("");
                chk = false;
                String Name = resultSet.getString("name");
                String FatherName = resultSet.getString("Father_Name");
                String cnic = resultSet.getString("CNIC");
                int Age = resultSet.getInt("Age");
                String Address = resultSet.getString("Address");
                String phnNum = resultSet.getString("Phone_Number");
                String dob = resultSet.getString("DOB");
                String Gender = resultSet.getString("Gender");
                String Specialization= resultSet.getString("Specialization");
                Image = resultSet.getBlob("image");
                String department = resultSet.getString("department");
                String Type = resultSet.getString("Type");
                String Scale = resultSet.getString("Scale");
                byte[] imageData = Image.getBytes(1, (int) Image.length());
                Image image = new Image(new ByteArrayInputStream(imageData));

                updatedName.setText(Name);
                updatedFatherName.setText(FatherName);
                updatedCNIC.setText(cnic);
                updatedAge.setText(String.valueOf(Age));
                updatedAddress.setText(Address);
                updatedPhnNum.setText(phnNum);
                updatedDOB.setValue(LocalDate.parse(dob));

                if (Gender.equals("Male")){
                    updatedMaleBtn.setSelected(true);
                }else{updatedFemaleBtn.setSelected(true);}

                updatedImageView.setImage(image);
                updatedDepartment.setValue(department);
                updatedType.setValue(Type);
                updatedScale.setValue(Scale);
                specialization.setText(Specialization);
            }
            if (chk){
                updatedPromptMsg.setText("No Faculty member found with provided ID");
                updatedName.clear();
                updatedFatherName.clear();
                updatedCNIC.clear();
                updatedAge.clear();
                updatedAddress.clear();
                specialization.clear();
                updatedPhnNum.clear();
                updatedDOB.setValue(null);
                genderGroup.selectToggle(null);
                updatedDepartment.getSelectionModel().clearSelection();
                updatedType.getSelectionModel().clearSelection();
                updatedScale.getSelectionModel().clearSelection();
                updatedImageView.setImage(null);
            }

        }catch (SQLException e){}
    }
    @FXML
    void updateImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );
        if (selectedFile != null) {
            imagePath = selectedFile.getPath();
            Image image = new Image(imagePath);
            updatedImageView.setImage(image);

        } else {
//            System.out.println("No file selected.");
        }
    }

    @FXML
    void saveInfo(ActionEvent event) throws FileNotFoundException, SQLException {
        if (updateIsValid()){
            validate();
            genderGroup = new ToggleGroup();
            updatedFemaleBtn.setToggleGroup(genderGroup);
            updatedMaleBtn.setToggleGroup(genderGroup);
            RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();
            String gender = selectedRadioButton.getText();
            LocalDate selectedDate = updatedDOB.getValue();
            String Name = updatedName.getText();
            String FatherName = updatedFatherName.getText();
            String cnic = updatedCNIC.getText();
            int Age = Integer.parseInt(updatedAge.getText());
            String Department = updatedDepartment.getValue();
            String Type = updatedType.getValue();
            String Scale = updatedScale.getValue();
            String Address = updatedAddress.getText();
            String Specialization = specialization.getText();
            String phnNum = updatedPhnNum.getText();
            String dob = selectedDate.toString();
            String id = updatedSearchFID.getText();
            String Course = addCourse.getValue();

            String query = "UPDATE faculty SET Name = ?, Father_Name = ?, CNIC = ?, Address = ?, Age = ?, Gender = ?," +
                    " DOB = ?, Phone_Number = ?, Department = ?, Specialization = ?, Scale = ?, Image = ?, Type = ?,Course =? WHERE ID = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, Name);
            statement.setString(2, FatherName);
            statement.setString(3, cnic);
            statement.setString(4, Address);
            statement.setInt(5, Age);
            statement.setString(6, gender);
            statement.setString(7, dob);
            statement.setString(8, phnNum);
            statement.setString(9, Department);
            statement.setString(10, Specialization);
            statement.setString(11, Scale);
            if (insertBtn.isPressed()) {
                File imageFile = new File(imagePath);
                fis = new FileInputStream(imageFile);
                statement.setBlob(12, fis);
            } else {
                statement.setBlob(12, Image);
            }
            statement.setString(13, Type);
            statement.setString(14, Course);
            statement.setString(15, id);
            statement.executeUpdate();

            confirmation();
        }
    }
    private boolean updateIsValid(){
        if (isAnyFieldEmpty()){
            updatedPromptMsg.setText("Kindly Fill the form Completely");
        } else if (!isNumericAge()) {
            updatedAge.clear();
            updatedAge.setPromptText("Enter Age in Integers");
        }
//        else if (!isNumericFee()) {
//            feePrompt.setText("*Invalid");
//        }
        return !isAnyFieldEmpty() && isNumericAge() ;//&& isNumericFee();
    }
    private void validate(){
        updatedPromptMsg.setText("");
//        agePrompt.setText("");
//        feePrompt.setText("");
    }
    private boolean isAnyFieldEmpty() {
        return     updatedName.getText().isEmpty()
                || updatedFatherName.getText().isEmpty()
                || updatedAddress.getText().isEmpty()
                || updatedCNIC.getText().isEmpty()
                || updatedAge.getText().isEmpty()
                || specialization.getText().isEmpty()
                || updatedPhnNum.getText().isEmpty()
                || updatedDOB.getValue() == null
                || (!updatedMaleBtn.isSelected() && !updatedFemaleBtn.isSelected()
                || updatedDepartment.getSelectionModel().isEmpty()
                || updatedScale.getSelectionModel().isEmpty()
                || updatedImageView.getImage() == null);
    }
    private boolean isNumericAge() {
        String checked = updatedAge.getText();
        return checked.matches("\\d+");
    }
//    private boolean isNumericFee() {
//        String checked = updatedFee.getText();
//        return checked.matches("\\d+");
//    }
    private void confirmation(){
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Information updated successfully!");
        successAlert.showAndWait();
        updatedName.clear();
        updatedFatherName.clear();
        updatedCNIC.clear();
        updatedAge.clear();
        updatedAddress.clear();
        updatedPhnNum.clear();
        updatedDOB.setValue(null);
        genderGroup.selectToggle(null);
        updatedDepartment.getSelectionModel().clearSelection();
        updatedScale.getSelectionModel().clearSelection();
        updatedImageView.setImage(null);
        specialization.clear();
    }
    private boolean isImageFile(File file) {
        List<String> imageExtensions = Arrays.asList(".png", ".jpg", ".gif", ".bmp", ".jpeg");
        String fileName = file.getName().toLowerCase();
        return imageExtensions.stream().anyMatch(fileName::endsWith);
    }

}
