package FormControllers;

import Classes.Department;
import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentForms {

    @FXML
    private TextField DCO;

    @FXML
    private TextField HOD;

    @FXML
    private TextField depID;

    @FXML
    private TextField name;

    @FXML
    private TextField fee;

    @FXML
    private Text promptMsg;

    @FXML
    private JFXButton submitBtn;

    @FXML
    private Text searchPrompt;

    @FXML
    private JFXButton removeBtn;

    @FXML
    private TextField searchDID;

    @FXML
    private JFXTextArea textArea;

    @FXML
    private TextField searchUpdatedDID;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private TextField updatedDCO;

    @FXML
    private TextField updatedHOD;

    @FXML
    private TextField updatedName;

    @FXML
    private Text updatedPromptMsg;

    @FXML
    private TextField updatedFee;

    @FXML
    private TextField updatedDepID;

    Connection connection =  DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public DepartmentForms() throws SQLException {
    }

    @FXML
    void removeSearch(MouseEvent event) throws SQLException {
        String query = "SELECT * FROM Department WHERE ID=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,searchDID.getText());
        resultSet = statement.executeQuery();
        boolean chk=true;

        while (resultSet.next()){
            chk = false;
            String Name = resultSet.getString("Name");
            String HOD = resultSet.getString("HO");
            String DCO = resultSet.getString("DCO");
            double Fee = resultSet.getInt("Fee");

            textArea.setText("Department ID: " + searchDID + "\nDepartment Name: " + Name +
                             "\nHOD: "+HOD + "\nDCO: " + DCO + "\nTuition Fee: " + Fee);
        }if (chk) {
            searchPrompt.setText("No Department found with provided ID");
            textArea.clear();
        }
        else searchPrompt.setText("");
    }

    @FXML
    void removeDepartment(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Department");
        alert.setContentText("Are you sure you want to remove this Department?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String query = "DELETE FROM department WHERE Id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, searchDID.getText());
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Department deleted successfully!");

                        successAlert.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                textArea.clear();
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
    void submitInfo(ActionEvent event) throws SQLException {

        if (inputIsValid()){
            if (facultyExists(HOD.getText()) && facultyExists(DCO.getText())) {
                String Name = name.getText();
                String hod = HOD.getText();
                String dco = DCO.getText();
                String ID = depID.getText();
                double Fee = Double.parseDouble(fee.getText());

                Department department = new Department(Name, hod, dco, ID, Fee);

            } else
                promptMsg.setText("Invalid Faculty ID for DCO or HOD");
        }else promptMsg.setText("Kindly fill the form properly");
    }



    @FXML
    void updateSearch(MouseEvent event) throws SQLException {
        String query = "SELECT * FROM Department WHERE ID=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,searchUpdatedDID.getText());
        resultSet = statement.executeQuery();
        boolean chk=true;

        while (resultSet.next()){
            chk = false;
            String Name = resultSet.getString("Name");
            String dco = resultSet.getString("DCO");
            String hod = resultSet.getString("HOD");
            double Fee = resultSet.getInt("Age");

            updatedName.setText(Name);
            updatedHOD.setText(hod);
            updatedDCO.setText(dco);
            updatedFee.setText(String.valueOf(Fee));
        }if (chk)searchPrompt.setText("No Department found with provided ID");
        else searchPrompt.setText("");
    }

    @FXML
    void updateInfo(ActionEvent event) throws SQLException {
        if (inputIsValid()){
            if (facultyExists(HOD.getText()) && facultyExists(DCO.getText())) {
                String Name = name.getText();
                String hod = HOD.getText();
                String dco = DCO.getText();
                String ID = depID.getText();
                double Fee = Double.parseDouble(fee.getText());

                String query = "UPDATE department SET Name = ?, HOD = ?, DCO = ?, Fee = ? WHERE ID = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, Name);
                statement.setString(2, hod);
                statement.setString(3, dco);
                statement.setDouble(4, Fee);
                statement.setString(5, ID);

                confirmation();
            } else
                promptMsg.setText("Invalid Faculty ID for DCO or HOD");
        }else promptMsg.setText("Kindly fill the form properly");

    }
    private boolean inputIsValid() {
        return     !name.getText().isEmpty()
                || !DCO.getText().isEmpty()
                || !HOD.getText().isEmpty()
                || !fee.getText().isEmpty()
                || !depID.getText().isEmpty();
    }

    private boolean facultyExists(String ID) throws SQLException {
        String query = "SELECT * FROM faculty WHERE ID=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,ID);
        resultSet = statement.executeQuery();

        return  resultSet.next();
    }
    private void confirmation(){
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Information updated successfully!");
        successAlert.showAndWait();
        updatedName.clear();
        updatedDCO.clear();
        updatedHOD.clear();
        updatedFee.clear();
        searchUpdatedDID.clear();
    }

}
