package lk.ijse.carRentalServiceCmjd102Udeeka.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public AnchorPane loginNode;
    public TextField txtUser;
    public TextField txtPassword;

    public void buttonLoginOnAction(ActionEvent actionEvent) throws IOException {
        String username = txtUser.getText();
        String password = txtPassword.getText();

        if (authenticateUser(username, password)) {
            navigateToDashboard();
        } else {
            new Alert(Alert.AlertType.ERROR, "User Name or Password Incorrect").show();
        }
    }

    private boolean authenticateUser(String username, String password) {
        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT customer_Password FROM newCustomer WHERE customer_Username = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("customer_Password");

                if (BCrypt.checkpw(password, storedPassword)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void navigateToDashboard() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carServiceDashboard_form.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) this.loginNode.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
    }

    public void buttonSignupOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/newCustomerLoginForm.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) this.loginNode.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Signup");
        stage.centerOnScreen();
    }
}
