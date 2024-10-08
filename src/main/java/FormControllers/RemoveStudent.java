package FormControllers;

import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class RemoveStudent {

    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    @FXML
    private JFXButton removeBtn;

    @FXML
    private ImageView removeImageView;

    @FXML
    private TextField searchSID;

    @FXML
    private Text stdSearchPrompt;

    @FXML
    private JFXTextArea textArea;

    String Status;

    public RemoveStudent() throws SQLException {
    }

//    @FXML
//    void removeSearch(KeyEvent event) {
//        if (event.getCode().equals(KeyCode.ENTER)){
//            removeSearch();
//        }
//    }

    @FXML
    void removeSearch(MouseEvent event) {
        try {
            String query = "SELECT * FROM student WHERE ID=?";
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
                int Semester = resultSet.getInt("Semester");
                Status = resultSet.getString("Status");
                int fee = resultSet.getInt("Fee");

                byte[] imageData = blob.getBytes(1, (int) blob.length());
                Image image = new Image(new ByteArrayInputStream(imageData));

                textArea.setText("ID: " + ID + "\nName: " + Name + "\nFatherName: " + FatherName +
                        "\nCNIC: " + cnic + "\nAge: " + Age + "\nDepartment: " + Department + "\nAddress: " + Address +
                        "\nPhone Number: " + phnNum + "\nDOB: " + dob + "\nGender: " + Gender + "\nSemester: " + Semester +
                        "\nStatus: " + Status + "\nFee: " + fee);
                removeImageView.setImage(image);

            }
            if (chk){
                stdSearchPrompt.setText("No student found with provided ID");
                textArea.clear();
                removeImageView.setImage(null);
            } else {
                stdSearchPrompt.setText("");
            }
        } catch (SQLException e) {
        }

    }

    @FXML
    void removeStudent(ActionEvent event) {
        String ID = searchSID.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Student");
        alert.setContentText("Are you sure you want to remove this student?");

        // Show confirmation dialog
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String query = "DELETE FROM student WHERE Id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, ID);
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Student deleted successfully!");

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
    @FXML
    void suspendStudent(ActionEvent event) {
        if ("Suspended".equals(Status)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("The student is already Suspended");
            alert.showAndWait();
        } else {
            try {
                connection = DBConnection.getConnection();
                String query = "UPDATE student SET Status = ? WHERE ID=?";
                statement = connection.prepareStatement(query);
                statement.setString(1, "Suspended");
                statement.setString(2, searchSID.getText());
                statement.executeUpdate();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Status updated successfully!");
                successAlert.showAndWait();
                textArea.clear();
                removeImageView.setImage(null);
            } catch (SQLException e) {
            }
        }
    }

    @FXML
    void reAdmit(ActionEvent event) {
        if ("Currently_Studding".equals(Status)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("The student is already Studding");
            alert.showAndWait();
        } else {
            try {
                connection = DBConnection.getConnection();
                String query = "UPDATE student SET Status = ? WHERE ID=?";
                statement = connection.prepareStatement(query);
                statement.setString(1, "Currently_Studding");
                statement.setString(2, searchSID.getText());
                statement.executeUpdate();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Status updated successfully!");
                successAlert.showAndWait();
                textArea.clear();
                removeImageView.setImage(null);
            } catch (SQLException e) {
            }
        }
    }
}
