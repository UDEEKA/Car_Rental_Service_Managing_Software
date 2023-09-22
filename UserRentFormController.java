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

import java.io.IOException;
import java.sql.*;

public class UserRentFormController {

    @FXML
    private AnchorPane userRentFormNode;

    @FXML
    private TextField txtCustomerAddress;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerNic;

    @FXML
    private TextField txtCustomerPhoneNumber;

    @FXML
    private TextField txtRentAdvance;

    @FXML
    private TextField txtRentBalance;

    @FXML
    private TextField txtRentDeposit;

    @FXML
    private TextField txtRentDuration;

    @FXML
    private TextField txtRentId;

    @FXML
    private TextField txtRentPerDay;

    @FXML
    private TextField txtRentTotal;

    @FXML
    private TextField txtVehicleBrand;

    @FXML
    private TextField txtVehicleID;

    @FXML
    private TextField txtVehicleModel;

    @FXML
    private TextField txtVehicleName;

    @FXML
    private TextField txtVehicleNumber;

    private double vehicleRent;
    private double vehicleDeposit;
    private double vehicleAdvancePayment;

    @FXML
    void buttonBackOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carServiceDashboard_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.userRentFormNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Service Dashboard");
    }

    @FXML
    void txtVehicleIDSearchOnAction(ActionEvent event) {

        String vehicleId = txtVehicleID.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM car WHERE vehicle_Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vehicleId);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {

                txtVehicleNumber.setText(resultSet.getString("vehicle_Number"));
                txtVehicleBrand.setText(resultSet.getString("vehicle_Brand"));
                txtVehicleModel.setText(resultSet.getString("vehicle_Model"));
                txtVehicleName.setText(resultSet.getString("vehicle_Name"));
                vehicleRent = resultSet.getDouble("vehicle_Rent");

            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM rentCarForm WHERE vehicle_Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vehicleId);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                txtCustomerId.setText(resultSet.getString("customer_ID"));
                txtCustomerName.setText(resultSet.getString("customer_Name"));

            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM customerRent WHERE vehicle_Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vehicleId);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                txtRentPerDay.setText(resultSet.getString("vehicle_Rent"));
                txtRentAdvance.setText(resultSet.getString("vehicle_AdvancePayment"));
                txtRentDeposit.setText(resultSet.getString("vehicle_RefundableDeposit"));
                txtRentDuration.setText(resultSet.getString("NumberOfRentDays"));
                txtRentTotal.setText(resultSet.getString("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            double vehicleAdvancePayment = Double.parseDouble(txtRentAdvance.getText());

            double total = vehicleRent * Integer.parseInt(txtRentDuration.getText());
            double balance = vehicleDeposit + vehicleAdvancePayment - total;

            txtRentTotal.setText(String.valueOf(total));
            txtRentDeposit.setText(String.valueOf(vehicleDeposit));
            txtRentBalance.setText(String.valueOf(balance));

        } catch (NumberFormatException e) {

            new Alert(Alert.AlertType.ERROR, "Invalid numeric input").show();
        }
    }

    @FXML
    public void buttonSaveOnAction(ActionEvent event) {
            String vehicleId = txtVehicleID.getText();
            String vehicleNumber = txtVehicleNumber.getText();
            String vehicleBrand = txtVehicleBrand.getText();
            String vehicleModel = txtVehicleModel.getText();
            String vehicleName = txtVehicleName.getText();
            String vehicleRent = txtRentPerDay.getText();
            String numberOfRentDays = txtRentDuration.getText();
            String rentTotal = txtRentTotal.getText();
            String advancePayment = txtRentAdvance.getText();
            String rentBalance = txtRentBalance.getText();
            String rentRefundableDeposit = txtRentDeposit.getText();
            String customerId = txtCustomerId.getText();
            String customerName = txtCustomerName.getText();

            try {
                Connection con = DbConnection.getInstance().getConnection();

                String sql = "INSERT INTO userRent (vehicle_Id, vehicle_Number, vehicle_Brand, vehicle_Model, vehicle_Name, vehicle_Rent, NumberOfRentDays, total, vehicle_AdvancePayment, balance, vehicle_RefundableDeposit, customer_ID, customer_Name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                pstm.setString(1, vehicleId);
                pstm.setString(2, vehicleNumber);
                pstm.setString(3, vehicleBrand);
                pstm.setString(4, vehicleModel);
                pstm.setString(5, vehicleName);
                pstm.setString(6, vehicleRent);
                pstm.setString(7, numberOfRentDays);
                pstm.setString(8, rentTotal);
                pstm.setString(9, advancePayment);
                pstm.setString(10, rentBalance);
                pstm.setString(11, rentRefundableDeposit);
                pstm.setString(12, customerId);
                pstm.setString(13, customerName);

                int rowsAffected = pstm.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = pstm.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        System.out.println("Generated ID: " + id);
                    }
                    clearFields();
                    new Alert(Alert.AlertType.CONFIRMATION, "Rent successfully saved").show();
                }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }

    }

    private void clearFields() {
        txtVehicleID.setText("");
        txtVehicleNumber.setText("");
        txtVehicleBrand.setText("");
        txtVehicleModel.setText("");
        txtVehicleName.setText("");
        txtRentPerDay.setText("");
        txtRentDuration.setText("");
        txtRentTotal.setText("");
        txtRentAdvance.setText("");
        txtRentBalance.setText("");
        txtRentDeposit.setText("");
        txtCustomerId.setText("");
        txtCustomerName.setText("");
    }
}
