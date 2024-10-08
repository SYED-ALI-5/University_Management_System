package FormControllers;

import DataBase.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddCourse {

    @FXML
    private TextField ID;

    @FXML
    private ComboBox<String> department;

    @FXML
    private ComboBox<Integer> creditHours;

    @FXML
    private ComboBox<Integer> level;

    @FXML
    private Text promptMsg;

    @FXML
    private TextField title;

    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public AddCourse() throws SQLException {
    }

    @FXML
    private void initialize() {
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT name FROM department";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String departmentName = resultSet.getString("name");
                department.getItems().add(departmentName);
            }

            ArrayList<Integer> semes = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                semes.add(i);
            }
            level.getItems().addAll(semes);
            creditHours.getItems().add(3);
            creditHours.getItems().add(4);

        } catch (SQLException e) {
        }
    }

    @FXML
    void submitInfo(ActionEvent event) {
        if (inputIsValid()) {
            String Name = title.getText();
            int cH = creditHours.getValue();
            String Department = department.getValue();
            int Level = level.getValue();
            String id = ID.getText();

            try{
                String countQuery = "SELECT * FROM courses WHERE department = ? AND level = ?";
                PreparedStatement countStatement = connection.prepareStatement(countQuery);
                countStatement.setString(1, Department);
                countStatement.setInt(2, Level);
                ResultSet resultSet = countStatement.executeQuery();

                int courseCount = 0;
                while (resultSet.next()) {
                    courseCount++;
                }

                if (courseCount < 5) {
                    String insertQuery = "INSERT INTO courses (ID, Title, Credit_Hours, Department, Level) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, id);
                    insertStatement.setString(2, Name);
                    insertStatement.setInt(3, cH);
                    insertStatement.setString(4, Department);
                    insertStatement.setInt(5, Level);
                    insertStatement.executeUpdate();

                    confirmation();
                } else {
                    promptMsg.setText("Maximum courses Limit reached : 5");
                }
            }catch (SQLException e){}
        }
    }

    private boolean inputIsValid() {
        return !title.getText().isEmpty()
                || !ID.getText().isEmpty()
                || !creditHours.getSelectionModel().isEmpty()
                || !department.getSelectionModel().isEmpty()
                || !level.getSelectionModel().isEmpty();
    }
    private void confirmation(){
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Course added successfully!");
        successAlert.showAndWait();
        title.clear();
        ID.clear();
        creditHours.getSelectionModel().clearSelection();
        department.getSelectionModel().clearSelection();
        level.getSelectionModel().clearSelection();
    }
}
