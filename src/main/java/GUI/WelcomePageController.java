package GUI;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomePageController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private ProgressBar progressBar;

    private double x=0;
    private double y=0;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GaussianBlur blur = new GaussianBlur(15);
        imageView.setEffect(blur);

        String progressBarStyle = "-fx-accent: black;";
        progressBar.setStyle(progressBarStyle);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> progressBar.setProgress(0.0)),
                new KeyFrame(Duration.seconds(1), e -> progressBar.setProgress(0.3)),
                new KeyFrame(Duration.seconds(3), e -> progressBar.setProgress(0.75)),
                new KeyFrame(Duration.seconds(6), e -> {progressBar.setProgress(1.0);
                    switchToLoginScene();
    }));
        timeline.play();
    }

    private void switchToLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginRoot = loader.load();
            Stage currentStage = (Stage) progressBar.getScene().getWindow();
            Scene loginScene = new Scene(loginRoot);
            Stage loginStage = new Stage();

            loginRoot.setOnMousePressed((MouseEvent event) ->{
                x=event.getSceneX();
                y=event.getSceneY();
            });

            loginRoot.setOnMouseDragged((MouseEvent event) ->{
                loginStage.setX(event.getScreenX()-x);
                loginStage.setY(event.getScreenY()-y);
                loginStage.setOpacity(0.8);
            });
            loginRoot.setOnMouseReleased((MouseEvent event) ->{
                loginStage.setOpacity(1);
            });

            currentStage.close();

            loginStage.setScene(loginScene);
            loginStage.setTitle("Login");
            loginStage.setResizable(false);
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}