package GUI;

import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private JFXButton btnClose,btnMin;
    @FXML
    private Pane homePane,studentPane,facultyPane,staffPane,departmentPane,coursesPane;

    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;

    public AdminController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPane(homePane);

    }

    @FXML
    private void handleMenuButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.getId().equals("btnHome")) {
            showPane(homePane);
        } else if (clickedButton.getId().equals("btnStudents")) {
            showPane(studentPane);
        } else if (clickedButton.getId().equals("btnFaculty")) {
            showPane(facultyPane);
        } else if (clickedButton.getId().equals("btnStaff")) {
            showPane(staffPane);
        } else if (clickedButton.getId().equals("btnDepartment")) {
            showPane(departmentPane);
        } else if (clickedButton.getId().equals("btnCourses")) {
            showPane(coursesPane);
        }
    }

    private void showPane(Pane paneToShow) {
        homePane.setVisible(paneToShow == homePane);
        studentPane.setVisible(paneToShow == studentPane);
        facultyPane.setVisible(paneToShow == facultyPane);
        staffPane.setVisible(paneToShow == staffPane);
        departmentPane.setVisible(paneToShow == departmentPane);
        coursesPane.setVisible(paneToShow == coursesPane);
    }

    @FXML
    void CloseButton(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void MinButton(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void LogOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Login Page");
            newStage.setScene(new Scene(root));
            newStage.show();

            Stage currentStage = (Stage) btnClose.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnAboutClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/AboutUs.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About Us");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


    @FXML
    void addNewStudent(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/addStudent.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Admission Form");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void removeStudent(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/removeStudent.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Suspend/Remove Student");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FXML
    void updateStudentInfo(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/updateStudent.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void addFaculty(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/addFaculty.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void removeFaculty(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/removeFaculty.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void updateFaculty(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/updateFaculty.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FXML
    void addNewStaff(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/addNewStaff.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void removeStaff(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/removeStaff.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void updateStaff(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/updateStaff.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void addCourse(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/addCourse.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void removeCourse(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/removeCourse.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void updateCourse(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/updateCourse.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void addDepartment(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/addDepartment.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void updateDepartment(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/updateDepartment.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    @FXML
    void removeDepartment(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/removeDepartment.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Student Info");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

}

