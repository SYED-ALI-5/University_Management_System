package FormControllers;

import DataBase.DBConnection;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class RemoveStaff {

    @FXML
    private ImageView removeImage;

    @FXML
    private Text searchPrompt;

    @FXML
    private TextField searchSID;

    @FXML
    private JFXTextArea textArea;

    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public RemoveStaff() throws SQLException {
    }

    @FXML
    void searchStaff(MouseEvent event) {
        try {
            String query = "SELECT * FROM staff WHERE ID=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, searchSID.getText());
            resultSet = statement.executeQuery();
            boolean chk=true;

            while (resultSet.next()) {
                chk = false;
                String ID = resultSet.getString("ID");
                String Name = resultSet.getString("name");
                String FatherName = resultSet.getString("Father_Name");
                String cnic = resultSet.getString("CNIC");
                int Age = resultSet.getInt("Age");
                String Department = resultSet.getString("Department");
                String Address = resultSet.getString("Address");
                String phnNum = resultSet.getString("Phone_Number");
                String dob = resultSet.getString("DOB");
                String Gender = resultSet.getString("Gender");
                Blob blob = resultSet.getBlob("image");
                String Status = resultSet.getString("Status");
                String Type = resultSet.getString("Type");
                String Speciality = resultSet.getString("Speciality");
                double salary = resultSet.getInt("Salary");
                int scale = resultSet.getInt("Scale");
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                Image image = new Image(new ByteArrayInputStream(imageData));
                removeImage.setImage(image);

                textArea.setText("ID: " + ID + "\nName: " + Name + "\nFatherName: " + FatherName +
                        "\nCNIC: " + cnic + "\nAge: " + Age + "\nDepartment: " + Department + "\nAddress: " + Address +
                        "\nPhone Number: " + phnNum + "\nDOB: " + dob + "\nGender: " + Gender +"\nScale: " + scale +
                        "\nSpecialization: " + Speciality +"\nType: " + Type +
                        "\nStatus: " + Status + "\nSalary: " + salary);

            }
            if (chk){
                searchPrompt.setText("No Faculty member found with provided ID");
                textArea.clear();
                removeImage.setImage(null);
            } else {
                searchPrompt.setText("");
            }
        } catch (SQLException e) {
        }
    }
    @FXML
    void removeStaff(ActionEvent event) {
        String ID = searchSID.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Staff");
        alert.setContentText("Are you sure you want to remove this Staff member?");

        // Show confirmation dialog
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String query = "DELETE FROM staff WHERE ID = ?";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, ID);
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Staff member removed successfully!");
                        successAlert.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                textArea.clear();
                                removeImage.setImage(null);
                            }
                        });
                    }
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}