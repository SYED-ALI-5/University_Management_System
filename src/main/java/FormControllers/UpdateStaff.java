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
import java.util.Arrays;
import java.util.List;

public class UpdateStaff {

    @FXML
    private JFXButton saveBtn,insertBtn;

    @FXML
    private ImageView updatedImage;

    @FXML
    private Text updatedPromptMsg;

    @FXML
    private TextField updatedAddress;

    @FXML
    private TextField updatedAge;

    @FXML
    private TextField updatedCNIC;

    @FXML
    private DatePicker updatedDOB;

    @FXML
    private TextField updatedFatherName;

    @FXML
    private RadioButton updatedFemaleBtn;

    @FXML
    private RadioButton updatedMaleBtn;

    @FXML
    private TextField updatedName;

    @FXML
    private TextField updatedPhnNum;

    @FXML
    private TextField updatedSearchSID;

    @FXML
    private ComboBox<String> updatedSpeciality;

    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    Blob Image;
    FileInputStream fis;
    ToggleGroup genderGroup;
    String imagePath;

    public UpdateStaff() throws SQLException {
    }

    @FXML
    private void initialize() {
        genderGroup = new ToggleGroup();
        updatedFemaleBtn.setToggleGroup(genderGroup);
        updatedMaleBtn.setToggleGroup(genderGroup);
        updatedDOB.setEditable(false);
        try {
            connection =  DBConnection.getConnection();
            String query = "SELECT speciality FROM Specialties";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String speciality = resultSet.getString("name");
                updatedSpeciality.getItems().add(speciality);
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
            updatedImage.setImage(image);

        } else {
//            System.out.println("No file selected.");
        }
    }

    @FXML
    void updateSearch(MouseEvent event) {
        try {
            String query = "SELECT * FROM staff WHERE ID=?";
            statement = connection.prepareStatement(query);
            statement.setString(1,updatedSearchSID.getText());
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
                String Speciality= resultSet.getString("Speciality");
                Image = resultSet.getBlob("image");
                byte[] imageData = Image.getBytes(1, (int) Image.length());
                javafx.scene.image.Image image = new Image(new ByteArrayInputStream(imageData));

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

                updatedImage.setImage(image);
                updatedSpeciality.setValue(Speciality);
            }
            if (chk){
                updatedPromptMsg.setText("No student found with provided ID");
                updatedName.clear();
                updatedFatherName.clear();
                updatedCNIC.clear();
                updatedAge.clear();
                updatedAddress.clear();
                updatedPhnNum.clear();
                updatedDOB.setValue(null);
                genderGroup.selectToggle(null);
                updatedSpeciality.getSelectionModel().clearSelection();
                updatedImage.setImage(null);
            }

        }catch (SQLException e){}
    }

    @FXML
    void saveInfo(ActionEvent event) throws SQLException, FileNotFoundException {
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
            String Address = updatedAddress.getText();
            String Speciality = updatedSpeciality.getValue();
            String phnNum = updatedPhnNum.getText();
            String dob = selectedDate.toString();
            String id = updatedSearchSID.getText();

            String query = "UPDATE staff SET Name = ?, Father_Name = ?, CNIC = ?, Address = ?, Age = ?, Gender = ?," +
                    " DOB = ?, Phone_Number = ?, Speciality = ?, Image = ?,Salary = ? WHERE ID = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, Name);
            statement.setString(2, FatherName);
            statement.setString(3, cnic);
            statement.setString(4, Address);
            statement.setInt(5, Age);
            statement.setString(6, gender);
            statement.setString(7, dob);
            statement.setString(8, phnNum);
            statement.setString(9, Speciality);
            if (insertBtn.isPressed()) {
                File imageFile = new File(imagePath);
                fis = new FileInputStream(imageFile);
                statement.setBlob(10, fis);
            } else {
                statement.setBlob(10, Image);
            }
            statement.setString(11, id);
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
                || updatedPhnNum.getText().isEmpty()
                || updatedDOB.getValue() == null
                || (!updatedMaleBtn.isSelected() && !updatedFemaleBtn.isSelected())
                || updatedSpeciality.getSelectionModel().isEmpty()
                || updatedImage.getImage() == null;
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
        updatedSpeciality.getSelectionModel().clearSelection();
        updatedImage.setImage(null);
    }
    private boolean isImageFile(File file) {
        List<String> imageExtensions = Arrays.asList(".png", ".jpg", ".gif", ".bmp", ".jpeg");
        String fileName = file.getName().toLowerCase();
        return imageExtensions.stream().anyMatch(fileName::endsWith);
    }

}
