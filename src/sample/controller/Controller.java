package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;
import sample.animations.Shake;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Button enterButton;

    @FXML
    private Button registerNowButton;

    @FXML
    void initialize() {
        enterButton.setOnAction(event -> {
            String passwordText = passwordField.getText().trim();
            String loginText = loginField.getText().trim();

            if (!loginText.equals("") && !passwordText.equals("")) loginUser(loginText, passwordText);
            else System.out.println("ERROR - login and password");
        });

        registerNowButton.setOnAction(event -> {
            openNewScene("/sample/view/signUp.fxml");
        });
    }



    private void loginUser(String loginText, String passwordText) {
        int counter = 0;
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(passwordText);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet resultSet = databaseHandler.getUser(user);

        try {
            while (resultSet.next()) counter++;
        } catch (SQLException e) { e.printStackTrace(); }

        if (counter >= 1) openNewScene("/sample/view/app.fxml");
        else {
            Shake shakeLogin = new Shake(loginField);
            Shake shakePassword = new Shake(passwordField);
            shakeLogin.playAnim();
            shakePassword.playAnim();
        }
    }



    private void openNewScene(String window) {
        // при нажатии на кнопку мы прячем окно
        // мы берем сцену на которой она находится
        // потом берем окно на которой она находится
        // и дальше уже это окно уже прячем
        registerNowButton.getScene().getWindow().hide();
        // далее нам нужно отобразить следующее нужное нам окно
        FXMLLoader fxmlLoader = new FXMLLoader();
        // устанавливаем локацию файла который нам надо загрузить
        fxmlLoader.setLocation(getClass().getResource(window));
        // а теперь запускаем его отображение
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent parent = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.showAndWait();
    }
}
