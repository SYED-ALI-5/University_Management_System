package GUI;

import Classes.Student;
import DataBase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.*;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class FacultyController {

    @FXML
    private TextField assignNum;

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXButton btnProfile;

    @FXML
    private JFXButton btnMin;

    @FXML
    private JFXButton btnTeaching;

    @FXML
    private Text courseID;

    @FXML
    private Text courseName;

    @FXML
    private Text validation;

    @FXML
    private TextField finalNum;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField midsNum;

    @FXML
    private JFXTextArea profile;

    @FXML
    private AnchorPane profilePane;

    @FXML
    private TextField quizNum;

    @FXML
    private TableView<Student> tableView;

    @FXML
    private TableColumn<Student, String> tbvDepartment;

    @FXML
    private TableColumn<Student, String> tbvID;

    @FXML
    private TableColumn<Student, String> tbvName;

    @FXML
    private TableColumn<Student, String> tbvSemes;

    @FXML
    private TableColumn<Student, String> tbvStatus;

    @FXML
    private AnchorPane teachingPane;
    Connection connection = DBConnection.getConnection();
    PreparedStatement statement;
    ResultSet resultSet;
    private String facID;
    private String id=null;


    public void setFacID(String fcID) throws SQLException {
        this.facID = fcID;
        initializeProfile(facID);
        initializeCourse(facID);
    }

    public FacultyController() throws SQLException {
    }

    @FXML
    private void initialize() {
        showPane(profilePane);
    }

    @FXML
    private void handleMenuButton(ActionEvent event) {
        // Get the button that was clicked
        Button clickedButton = (Button) event.getSource();

        // Depending on the clicked button, show the respective pane
        if (clickedButton.getId().equals("btnProfile")) {
            showPane(profilePane);
        } else if (clickedButton.getId().equals("btnTeaching")) {
            showPane(teachingPane);
        }
    }

    private void showPane(Pane paneToShow) {
        // Hide all panes except the one to be shown
        profilePane.setVisible(paneToShow == profilePane);
        teachingPane.setVisible(paneToShow == teachingPane);
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
        Stage stage = (Stage) btnClose.getScene().getWindow();

    }

    @FXML
    void updateMarks(ActionEvent event) throws SQLException {
        if (assignNum.getText().isEmpty() && quizNum.getText().isEmpty() && midsNum.getText().isEmpty() && finalNum.getText().isEmpty()) {
            validation.setText("Enter all marks to update!");
        } else {
            int aMarks = Integer.parseInt(assignNum.getText());
            int qMarks = Integer.parseInt(quizNum.getText());
            int mMarks = Integer.parseInt(midsNum.getText());
            int fMarks = Integer.parseInt(finalNum.getText());
            int totalMarks = aMarks + mMarks + qMarks + fMarks;

            Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
//
            if (selectedStudent != null) {
                String studentID = selectedStudent.getId();

                String selectionQuery = "Select * from Student_courses where Student_Id = ? AND Course_Id = ?";
                String updateQuery = "Update Student_courses Set Marks = ? where Student_Id = ? AND Course_Id = ?";

                PreparedStatement selectStatement = connection.prepareStatement(selectionQuery);
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                selectStatement.setString(1, studentID);
                selectStatement.setString(2, id);

                resultSet = selectStatement.executeQuery();
                while (resultSet.next()) {
                    updateStatement.setInt(1, totalMarks);
                    updateStatement.setString(2, studentID);
                    updateStatement.setString(3, id);
                    updateStatement.executeUpdate();
                }
            } else {
            }


        }

    }
    private void initializeProfile(String fcID){
        try {
            String query = "SELECT * FROM faculty WHERE ID=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, fcID);
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
                String Status = resultSet.getString("Status");
                String Type = resultSet.getString("Type");
                String Specialization = resultSet.getString("Specialization");
                double salary = resultSet.getInt("Salary");
                int scale = resultSet.getInt("Scale");
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);

                profile.setText("ID: " + ID + "\n\nName: " + Name + "\n\nFatherName: " + FatherName +
                        "\n\nCNIC: " + cnic + "\nAge: " + Age + "\n\nDepartment: " + Department + "\n\nAddress: " + Address +
                        "\n\nPhone Number: " + phnNum + "\n\nDOB: " + dob + "\n\nGender: " + Gender +"\n\nScale: " + scale +
                        "\nSpecialization: " + Specialization +"\n\nType: " + Type +
                        "\n\nStatus: " + Status + "\n\nSalary: " + salary);

            }
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
        }
    }
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    private void initializeCourse(String facID) throws SQLException {
        String query = "SELECT * FROM faculty WHERE ID = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,facID);
        ResultSet resultSet = statement.executeQuery();
        String title;
        while (resultSet.next()){
             title= resultSet.getString("Course");
             courseName.setText(title);
        }
        String query2 = "SELECT ID FROM Courses";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        ResultSet resultSet2 = statement2.executeQuery();
        while (resultSet2.next()){
            id= resultSet2.getString("ID");
            courseID.setText(id);
        }
        String query3 = "SELECT student_id FROM student_courses WHERE course_id =?";
        PreparedStatement statement3 = connection.prepareStatement(query3);
        statement3.setString(1,id);
        ResultSet resultSet3 = statement3.executeQuery();
        while (resultSet3.next()){
            String stdID= resultSet3.getString("student_id");
            String query4 = "SELECT * FROM Student WHERE ID =?";
            PreparedStatement statement4 = connection.prepareStatement(query4);
            statement4.setString(1,stdID);
            ResultSet resultSet4 = statement4.executeQuery();
            while (resultSet4.next()){
                String name = resultSet4.getString("name");
                String dep = resultSet4.getString("department");
                String status = resultSet4.getString("Status");
                int  smes = resultSet4.getInt("Semester");
                Student std = new Student(stdID,name,dep,smes,status);
                studentList.add(std);
            }
            tableView.setItems(studentList);

            tbvID.setCellValueFactory(new PropertyValueFactory<>("id"));
            tbvName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tbvDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
            tbvSemes.setCellValueFactory(new PropertyValueFactory<>("semester"));
            tbvStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        }

    }

}

