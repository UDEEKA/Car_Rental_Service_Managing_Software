package lk.ijse.carRentalServiceCmjd102Udeeka.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class carServiceDashboardFormController {

    @FXML
    private AnchorPane carServiceDashboardNode;

    @FXML
    void buttonCarManageOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carManage_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Manage");
    }

    @FXML
    void buttonCustomerManageOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/customerManage_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Customer Manage");
    }

    @FXML
    void buttonRentACarOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/rentCar_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Rent a Car");

    }

    public void buttonUserRentFormAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/userRent_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("User Rent Form");
    }


    @FXML
    void buttonReturnVehicleAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/returnProcess_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("User Rent Form");

    }

    @FXML
    void buttonOverdueRentalOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/rentalController.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Overdue Rentals");
    }
    public void buttonLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.carServiceDashboardNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Login Form");
    }
}
