package lk.ijse.carRentalServiceCmjd102Udeeka.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.Car;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class CarAddFormController {
    public TextField txtCarYear;
    public AnchorPane addCarRoot;
    public TextField txtCarBrand;
    public TextField txtCarModel;
    public TextField txtCarName;
    public TextField txtCarRent;
    public TextField txtCarId;
    public TextField txtCarNumber1;
    public TextField txtCarNumber2;
    public TextField txtCarDepositAmount;
    public TextField txtCarAdvancePaymentAmount;


    public void btnSaveNewCarOnAction(ActionEvent actionEvent) {
        String id = txtCarId.getText();
        String number1 = txtCarNumber1.getText();
        String number2 = txtCarNumber2.getText();
        String brand = txtCarBrand.getText();
        String model = txtCarModel.getText();
        String name = txtCarName.getText();
        int year = Integer.parseInt(txtCarYear.getText());
        double rent = Double.parseDouble(txtCarRent.getText());
        double depositAmount = Double.parseDouble(txtCarDepositAmount.getText());
        double advancePayment = Double.parseDouble(txtCarAdvancePaymentAmount.getText());

        if (!isValidNumber1(number1) || !isValidNumber2(number2)) {
            new Alert(Alert.AlertType.ERROR, "Invalid vehicle number format. Please check and try again.").show();
            return;
        }

        String vehicleNumber = number1 + "-" + number2;

        if (year < 1990 || year > LocalDate.now().getYear()) {
            new Alert(Alert.AlertType.ERROR, "Invalid car year. Please enter a year between 1980 and " + LocalDate.now().getYear() + ".").show();
            return;
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO car (vehicle_Id, vehicle_Number, vehicle_Brand, vehicle_Model, vehicle_Name, vehicle_Year, vehicle_Rent, vehicle_RefundableDeposit, vehicle_AdvancePayment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);
            pstm.setString(2, vehicleNumber);
            pstm.setString(3, brand);
            pstm.setString(4, model);
            pstm.setString(5, name);
            pstm.setInt(6, year);
            pstm.setDouble(7, rent);
            pstm.setDouble(8, depositAmount);
            pstm.setDouble(9, advancePayment);

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Car Added Successfully").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean isValidNumber1(String number1) {
        return Pattern.matches("^[A-Za-z]{3}$|^[0-9]{2}$", number1);
    }

    private boolean isValidNumber2(String number2) {
        return Pattern.matches("^[0-9]{4}$", number2);
    }

    private void clearFields() {
        txtCarId.setText("");
        txtCarNumber1.setText("");
        txtCarNumber2.setText("");
        txtCarBrand.setText("");
        txtCarModel.setText("");
        txtCarName.setText("");
        txtCarYear.setText("");
        txtCarRent.setText("");
        txtCarDepositAmount.setText("");
        txtCarAdvancePaymentAmount.setText("");

    }

    public void btnClearNewCarOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carManage_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.addCarRoot.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Manage");

    }
}
