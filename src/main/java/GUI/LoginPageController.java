package GUI;

import DataBase.DBConnection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    @FXML
    private ToggleButton adminBtn;

    @FXML
    private ToggleButton facultyBtn;

    @FXML
    private ToggleButton stdBtn;

    @FXML
    private AnchorPane adminInfo;

    @FXML
    private AnchorPane studentInfo;

    @FXML
    private AnchorPane facultyInfo;

    private FadeTransition fadeTransition;

    @FXML
    private Text aMessage;
    @FXML
    private Text sMessage;
    @FXML
    private Text fMessage;

    @FXML
    private PasswordField aPassword;
    @FXML
    private PasswordField sPassword;
    @FXML
    private PasswordField fPassword;

    @FXML
    private TextField aUserName;
    @FXML
    private TextField sUserName;
    @FXML
    private TextField fUserName;

    @FXML
    private ImageView imgView;
    @FXML
    private ImageView stdImgView;
    @FXML
    private ImageView fImgView;

    private double x=0;
    private double y=0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setEffect(new GaussianBlur(20));
        stdImgView.setEffect(new GaussianBlur(15));
        fImgView.setEffect(new GaussianBlur(15));

        ToggleGroup toggleGroup = new ToggleGroup();
        adminBtn.setToggleGroup(toggleGroup);
        stdBtn.setToggleGroup(toggleGroup);
        facultyBtn.setToggleGroup(toggleGroup);
        adminBtn.setSelected(true);

        studentInfo.setVisible(false);
        facultyInfo.setVisible(false);


        adminBtn.setOnAction(event -> handleToggleButton(adminInfo));
        stdBtn.setOnAction(event -> handleToggleButton(studentInfo));
        facultyBtn.setOnAction(event -> handleToggleButton(facultyInfo));


        fadeTransition = new FadeTransition(Duration.millis(500));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
    }


    @FXML
    void adminLogin(ActionEvent event){
        try(Connection connection =  DBConnection.getConnection()) {
            String query = "SELECT * FROM InformationUniversity WHERE Username = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,aUserName.getText());
            statement.setString(2,aPassword.getText());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                Node sourceNode = (Node) event.getSource();
                Stage currentStage = (Stage) sourceNode.getScene().getWindow();
                currentStage.close(); // Close the current stage

                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/adminDashBoard.fxml"));
                    Parent root = loader.load();
                    Stage newStage = new Stage();

                    root.setOnMousePressed((MouseEvent e) ->{
                        x=e.getSceneX();
                        y=e.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent e) ->{
                        newStage.setX(e.getScreenX()-x);
                        newStage.setY(e.getScreenY()-y);
                        newStage.setOpacity(0.8);
                    });

                    root.setOnMouseReleased((MouseEvent e) ->{
                        newStage.setOpacity(1);
                    });

                    newStage.setResizable(false);
                    newStage.initStyle(StageStyle.TRANSPARENT);
                    newStage.setTitle("Admin Portal");
                    newStage.setScene(new Scene(root));
                    newStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                aMessage.setText("Login Failed..!");
            }

        } catch (SQLException e){
            System.out.println(e);
        }
    }


    @FXML
    void stdLogin(ActionEvent event){
        try(Connection connection =  DBConnection.getConnection()) {
            String query = "SELECT * FROM student WHERE ID = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,sUserName.getText());
            statement.setString(2,sPassword.getText());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                Node sourceNode = (Node) event.getSource();
                Stage currentStage = (Stage) sourceNode.getScene().getWindow();
                currentStage.close(); // Close the current stage

                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/stdDashBoard.fxml"));
                    Parent root = loader.load();
                    Stage newStage = new Stage();

                    root.setOnMousePressed((MouseEvent e) ->{
                        x=e.getSceneX();
                        y=e.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent e) ->{
                        newStage.setX(e.getScreenX()-x);
                        newStage.setY(e.getScreenY()-y);
                        newStage.setOpacity(0.8);
                    });

                    root.setOnMouseReleased((MouseEvent e) ->{
                        newStage.setOpacity(1);
                    });

                    newStage.setResizable(false);
                    newStage.initStyle(StageStyle.TRANSPARENT);

                    StdController std = loader.getController();
                    std.setStdID(sUserName.getText());

                    newStage.initModality(Modality.APPLICATION_MODAL);
                    newStage.setTitle("Student Portal");
                    newStage.setScene(new Scene(root));
                    newStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                sMessage.setText("Login Failed..!");
            }

        }catch (SQLException e){
            System.out.println(e);
        }
    }


    @FXML
    void facLogin(ActionEvent event){
        try(Connection connection =  DBConnection.getConnection()) {
            String query = "SELECT * FROM faculty WHERE ID = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,fUserName.getText());
            statement.setString(2,fPassword.getText());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                Node sourceNode = (Node) event.getSource();
                Stage currentStage = (Stage) sourceNode.getScene().getWindow();
                currentStage.close();

                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FacultyDashBoard.fxml"));
                    Parent root = loader.load();

                    FacultyController fac = loader.getController();
                    fac.setFacID(fUserName.getText());

                    Stage newStage = new Stage();

                    root.setOnMousePressed((MouseEvent e) ->{
                        x=e.getSceneX();
                        y=e.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent e) ->{
                        newStage.setX(e.getScreenX()-x);
                        newStage.setY(e.getScreenY()-y);
                        newStage.setOpacity(0.8);
                    });

                    root.setOnMouseReleased((MouseEvent e) ->{
                        newStage.setOpacity(1);
                    });

                    newStage.setResizable(false);
                    newStage.initStyle(StageStyle.TRANSPARENT);

                    newStage.initModality(Modality.APPLICATION_MODAL);
                    newStage.setTitle("Faculty Form");
                    newStage.setScene(new Scene(root));
                    newStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                fMessage.setText("Login Failed..!");
            }

        }catch (SQLException e){
            System.out.println(e);
        }
    }


    private void handleToggleButton(AnchorPane pane) {
        if (pane == adminInfo && adminBtn.isSelected()) {
            showWithFadeTransition(adminInfo);
            studentInfo.setVisible(false);
            facultyInfo.setVisible(false);
        } else if (pane == studentInfo && stdBtn.isSelected()) {
            showWithFadeTransition(studentInfo);
            adminInfo.setVisible(false);
            facultyInfo.setVisible(false);
        } else if (pane == facultyInfo && facultyBtn.isSelected()) {
            showWithFadeTransition(facultyInfo);
            adminInfo.setVisible(false);
            studentInfo.setVisible(false);
        }
    }

    private void showWithFadeTransition(AnchorPane pane) {
        fadeTransition.setNode(pane);
        pane.setVisible(true);
        fadeTransition.play();
    }

    @FXML
    void cancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
