package FormControllers;

import DataBase.DBConnection;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.*;

public class RemoveCourse {

    @FXML
    private Text removePrompt;

    @FXML
    private TextField search;

    @FXML
    private JFXTextArea textArea;

    @FXML
    void removeSearch(MouseEvent event) {
        try{
            Connection connection = DBConnection.getConnection();
            String query = "SELECT * FROM Department WHERE ID=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, search.getText());
            ResultSet resultSet = statement.executeQuery();
            boolean chk = true;

            while (resultSet.next()) {
                chk = false;
                String ID = search.getText();
                String title = resultSet.getString("Title");
                int creditHours = resultSet.getInt("Credit_Hours");
                String department = resultSet.getString("Department");
                int level = resultSet.getInt("Level");

                textArea.setText("Course ID: " + ID + "\nCourse Title: " + title +
                        "\nCredit Hours: " + creditHours + "\nDepartment: " + department
                        + "Level: " + level);
            }
            if (chk) {
                removePrompt.setText("No Course found with provided ID");
                textArea.clear();
            }
            else removePrompt.setText("");
            statement.close();
        }catch (SQLException e){}
    }
    @FXML
    void removeCourse(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Course");
        alert.setContentText("Are you sure you want to remove this Course?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection connection = DBConnection.getConnection();
                    String query = "DELETE FROM courses WHERE Id = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, search.getText());
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Course deleted successfully!");

                        successAlert.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                textArea.clear();
                                search.clear();
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
