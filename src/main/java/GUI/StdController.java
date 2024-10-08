package GUI;

import Classes.Course;
import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

public class StdController {

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXButton btnMin;

    @FXML
    private JFXButton btnProfile;

    @FXML
    private JFXButton btnProgress;


    @FXML
    private ImageView imageView;


    @FXML
    private Label obtainedMarks;

    @FXML
    private JFXTextArea profile;

    @FXML
    private AnchorPane profilePane;

    @FXML
    private AnchorPane progressPane;

    @FXML
    private Text semester;

    @FXML
    private TableView<Course> tableView;

    @FXML
    private TableColumn<Course, String> tbvCID;

    @FXML
    private TableColumn<Course, String> tbvCh;

    @FXML
    private TableColumn<Course, String> tbvMarks;

    @FXML
    private TableColumn<Course, String> tbvTitle;

    @FXML
    private Text totalCourses;

    @FXML
    private Label totalMarks;
    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;
    private String stdID;

    public void setStdID(String ID) throws SQLException {
        stdID = ID;
        initializeProfile(stdID);
        initializeCourses(stdID);
    }


    public StdController() throws SQLException {
    }

    @FXML
    private void initialize() {
        profile.setEditable(false);
        showPane(profilePane);
//        initializeProfile();
    }

    @FXML
    private void handleMenuButton(ActionEvent event) {
        // Get the button that was clicked
        Button clickedButton = (Button) event.getSource();

        // Depending on the clicked button, show the respective pane
        if (clickedButton.getId().equals("btnProfile")) {
            showPane(profilePane);
        } else if (clickedButton.getId().equals("btnProgress")) {
            showPane(progressPane);
        }
    }

    private void showPane(Pane paneToShow) {
        profilePane.setVisible(paneToShow == profilePane);
        progressPane.setVisible(paneToShow == progressPane);
    }


    @FXML
    void CloseButton(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
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
    void MinButton(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.setIconified(true);
    }
    private void initializeProfile(String stdID){
        try {
            String query = "SELECT * FROM student WHERE ID=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, stdID);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
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
                String Status = resultSet.getString("Status");
                int fee = resultSet.getInt("Fee");

                byte[] imageData = blob.getBytes(1, (int) blob.length());
                Image image = new Image(new ByteArrayInputStream(imageData));

                profile.setText("ID: " + ID + "\n\nName: " + Name + "\n\nFatherName: " + FatherName +
                        "\n\nCNIC: " + cnic + "\n\nAge: " + Age + "\n\nDepartment: " + Department + "\n\nAddress: " + Address +
                        "\nPhone Number: " + phnNum + "\nDOB: " + dob + "\nGender: " + Gender + "\nSemester: " + Semester +
                        "\n\nStatus: " + Status + "\n\nFee: " + fee);
                imageView.setImage(image);

            }
    } catch (SQLException | RuntimeException e) {
        }
    }
    private ObservableList<Course> courseList = FXCollections.observableArrayList(); // Create an ObservableList

    private void initializeCourses(String stdID) throws SQLException {
        String query3 = "SELECT course_id FROM student_courses WHERE student_id =?";
        PreparedStatement statement3 = connection.prepareStatement(query3);
        statement3.setString(1,stdID);
        ResultSet resultSet3 = statement3.executeQuery();
        while (resultSet3.next()){
            String id= resultSet3.getString("course_id");
            String query4 = "SELECT * FROM courses WHERE ID =?";
            PreparedStatement statement4 = connection.prepareStatement(query4);
            statement4.setString(1,id);
            ResultSet resultSet4 = statement4.executeQuery();
            while (resultSet4.next()){
                String title = resultSet4.getString("Title");
                String cH = String.valueOf(resultSet4.getInt("Credit_Hours"));

                String selectionQuery = "Select marks from Student_courses where Student_Id = ? AND Course_Id = ?";

                PreparedStatement selectStatement = connection.prepareStatement(selectionQuery);

                selectStatement.setString(1, stdID);
                selectStatement.setString(2, id);

                resultSet = selectStatement.executeQuery();
                String Marks=null;
                while (resultSet.next()) {
                   Marks = String.valueOf(resultSet.getInt("Marks"));
                }

                Course crc = new Course(id,title,cH,Marks);
                courseList.add(crc);
            }
            tableView.setItems(courseList);

            tbvCID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            tbvTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            tbvCh.setCellValueFactory(new PropertyValueFactory<>("creditHr"));
            tbvMarks.setCellValueFactory(new PropertyValueFactory<>("marks"));
        }

    }
    }

