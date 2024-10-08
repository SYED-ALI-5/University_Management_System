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

public class RemoveFaculty {

    @FXML
    private ImageView removeImageView;

    @FXML
    private TextField searchFID;

    @FXML
    private Text searchPrompt;

    @FXML
    private JFXTextArea textArea;

    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public RemoveFaculty() throws SQLException {}

    @FXML
    void removeSearch(MouseEvent event) {
        try {
            String query = "SELECT * FROM faculty WHERE ID=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, searchFID.getText());
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
                String Specialization = resultSet.getString("Specialization");
                double salary = resultSet.getInt("Salary");
                int scale = resultSet.getInt("Scale");
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                Image image = new Image(new ByteArrayInputStream(imageData));
                removeImageView.setImage(image);

                textArea.setText("ID: " + ID + "\nName: " + Name + "\nFatherName: " + FatherName +
                        "\nCNIC: " + cnic + "\nAge: " + Age + "\nDepartment: " + Department + "\nAddress: " + Address +
                        "\nPhone Number: " + phnNum + "\nDOB: " + dob + "\nGender: " + Gender +"\nScale: " + scale +
                        "\nSpecialization: " + Specialization +"\nType: " + Type +
                        "\nStatus: " + Status + "\nSalary: " + salary);

            }
            if (chk){
                searchPrompt.setText("No Faculty member found with provided ID");
                textArea.clear();
                removeImageView.setImage(null);
            } else {
                searchPrompt.setText("");
            }
        } catch (SQLException e) {
        }

    }
    @FXML
    void removeFaculty(ActionEvent event) {
        String ID = searchFID.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Faculty");
        alert.setContentText("Are you sure you want to remove this Faculty member?");

        // Show confirmation dialog
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String query = "DELETE FROM faculty WHERE ID = ?";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, ID);
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Faculty member removed successfully!");
                        successAlert.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                textArea.clear();
                                removeImageView.setImage(null);
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
