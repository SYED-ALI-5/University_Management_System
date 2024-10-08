package FormControllers;

import Classes.Student;
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


public class UpdateStudent{
    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private DatePicker updatedDOB;

    @FXML
    private ComboBox<String> updatedDepartment;

    @FXML
    private RadioButton updatedFemaleBtn;

    @FXML
    private ImageView updatedImageView;

    @FXML
    private RadioButton updatedMaleBtn;

    @FXML
    private Text updatedPromptMsg,agePrompt,feePrompt;

    @FXML
    private JFXButton insertBtn;

    Blob Image;
    FileInputStream fis;
    int Semester;
    ToggleGroup genderGroup;
    String imagePath;

    @FXML
    private TextField updatedName,updatedFatherName,updatedPhoneNumber,updatedAddress,updatedAge,
            updatedCNIC,updatedFee,updatedSearchSID;

    public UpdateStudent() throws SQLException {
    }


    @FXML
    private void initialize() {

        genderGroup = new ToggleGroup();
        updatedFemaleBtn.setToggleGroup(genderGroup);
        updatedMaleBtn.setToggleGroup(genderGroup);
        updatedDOB.setEditable(false);
        try {
            connection =  DBConnection.getConnection();

            String query1 = "SELECT name FROM department";
            statement = connection.prepareStatement(query1);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String departmentName = resultSet.getString("name");
                updatedDepartment.getItems().add(departmentName);
            }
        }catch (SQLException e){}
    }

    @FXML
    void updatedSearch(MouseEvent event) {
        try {
            String query = "SELECT * FROM student WHERE ID=?";
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
                Image = resultSet.getBlob("image");
                int fee = resultSet.getInt("Fee");
                Semester = resultSet.getInt("Semester");
                String stdDep = resultSet.getString("department");

                byte[] imageData = Image.getBytes(1, (int) Image.length());
                Image image = new Image(new ByteArrayInputStream(imageData));
                updatedName.setText(Name);
                updatedFatherName.setText(FatherName);
                updatedCNIC.setText(cnic);
                updatedAge.setText(String.valueOf(Age));
                updatedAddress.setText(Address);
                updatedPhoneNumber.setText(phnNum);
                updatedDOB.setValue(LocalDate.parse(dob));

                if (Gender.equals("Male")){
                    updatedMaleBtn.setSelected(true);
                }else{updatedFemaleBtn.setSelected(true);}

                updatedFee.setText(String.valueOf(fee));
                updatedImageView.setImage(image);
                updatedDepartment.setValue(stdDep);
            }
            if (chk){
                updatedPromptMsg.setText("No student found with provided ID");
                updatedName.clear();
                updatedFatherName.clear();
                updatedCNIC.clear();
                updatedAge.clear();
                updatedAddress.clear();
                updatedPhoneNumber.clear();
                updatedDOB.setValue(null);
                genderGroup.selectToggle(null);
                updatedDepartment.getSelectionModel().clearSelection();
                updatedImageView.setImage(null);
                updatedFee.clear();
            }

        }catch (SQLException e){}
    }

    @FXML
    void updateImage(ActionEvent event) throws FileNotFoundException {
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
            String Department = updatedDepartment.getValue();
            String Address = updatedAddress.getText();
            String phnNum = updatedPhoneNumber.getText();
            String dob = selectedDate.toString();
            int fee = Integer.parseInt(updatedFee.getText());
            String id = updatedSearchSID.getText();


            String query = "UPDATE student SET Name = ?, Father_Name = ?, CNIC = ?, Address = ?, Age = ?, Gender = ?," +
                    " DOB = ?, Phone_Number = ?, Department = ?, Semester = ?, Fee = ?, Image = ? WHERE ID = ?";
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
            statement.setInt(10, Semester);
            statement.setInt(11, fee);
            if (insertBtn.isPressed()) {
                File imageFile = new File(imagePath);
                fis = new FileInputStream(imageFile);
                statement.setBlob(12, fis);
            } else {
                statement.setBlob(12, Image);
            }
            statement.setString(13, id);
            statement.executeUpdate();

            confirmation();
        }
    }
    private boolean updateIsValid(){
        if (isAnyFieldEmpty()){
            updatedPromptMsg.setText("Kindly Fill the form Completely");
        } else if (!isNumericAge()) {
            agePrompt.setText("Enter valid Age");
        }else if (!isNumericFee()) {
            feePrompt.setText("*Invalid");
        }
        return !isAnyFieldEmpty() && isNumericAge() && isNumericFee();
    }
    private void validate(){
        updatedPromptMsg.setText("");
        agePrompt.setText("");
        feePrompt.setText("");
    }
    private boolean isAnyFieldEmpty() {
        return     updatedName.getText().isEmpty()
                || updatedFatherName.getText().isEmpty()
                || updatedAddress.getText().isEmpty()
                || updatedCNIC.getText().isEmpty()
                || updatedAge.getText().isEmpty()
                || updatedFee.getText().isEmpty()
                || updatedPhoneNumber.getText().isEmpty()
                || updatedDOB.getValue() == null
                || (!updatedMaleBtn.isSelected() && !updatedFemaleBtn.isSelected()
                || updatedDepartment.getSelectionModel().isEmpty()
                || updatedImageView.getImage() == null);
    }
    private boolean isNumericAge() {
        String checked = updatedAge.getText();
        return checked.matches("\\d+");
    }
    private boolean isNumericFee() {
        String checked = updatedFee.getText();
        return checked.matches("\\d+");
    }
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
        updatedPhoneNumber.clear();
        updatedDOB.setValue(null);
        genderGroup.selectToggle(null);
        updatedDepartment.getSelectionModel().clearSelection();
        updatedImageView.setImage(null);
        updatedFee.clear();
    }
    private boolean isImageFile(File file) {
        List<String> imageExtensions = Arrays.asList(".png", ".jpg", ".gif", ".bmp", ".jpeg");
        String fileName = file.getName().toLowerCase();
        return imageExtensions.stream().anyMatch(fileName::endsWith);
    }
}