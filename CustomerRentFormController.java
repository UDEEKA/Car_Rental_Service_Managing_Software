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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRentFormController {
    public AnchorPane customerRentFormNode;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtVehicleId;
    public TextField txtVehicleNumber;
    public TextField txtVehicleBrand;
    public TextField txtVehicleModel;
    public TextField txtVehicleName;
    public TextField txtVehicleRent;
    public TextField txtNumberOfRentDays;
    public TextField txtAdvancePayment;
    public TextField txtTotalRent;
    public TextField txtDepositAmount;

    public void buttonCustomerRentCarOnAction(ActionEvent actionEvent) {
        saveCustomerRentData();
    }

    public void buttonCancelRentOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/rentCar_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.customerRentFormNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Customer Rent Form");
    }

    public void txtCustomerIdOnAction(ActionEvent actionEvent) {
        String customerId = txtCustomerId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM rentCarForm WHERE customer_ID = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, customerId);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String vehicleId = rs.getString("vehicle_Id");
                int numberOfRentDays = rs.getInt("NumberOfRentDays");

                txtVehicleId.setText(vehicleId);
                txtNumberOfRentDays.setText(String.valueOf(numberOfRentDays));

                String vehicleSql = "SELECT * FROM car WHERE vehicle_Id = ?";
                PreparedStatement vehiclePstmt = con.prepareStatement(vehicleSql);
                vehiclePstmt.setString(1, vehicleId);
                ResultSet vehicleResultSet = vehiclePstmt.executeQuery();

                if (vehicleResultSet.next()) {
                    txtVehicleNumber.setText(vehicleResultSet.getString("vehicle_Number"));
                    txtVehicleBrand.setText(vehicleResultSet.getString("vehicle_Brand"));
                    txtVehicleModel.setText(vehicleResultSet.getString("vehicle_Model"));
                    txtVehicleName.setText(vehicleResultSet.getString("vehicle_Name"));
                    txtVehicleRent.setText(vehicleResultSet.getString("vehicle_Rent"));
                    txtDepositAmount.setText(vehicleResultSet.getString("vehicle_RefundableDeposit"));
                    txtAdvancePayment.setText(vehicleResultSet.getString("vehicle_AdvancePayment"));

                    double vehicleRent = Double.parseDouble(txtVehicleRent.getText());
                    double total = numberOfRentDays * vehicleRent;
                    txtTotalRent.setText(String.valueOf(total));
                } else {
                    new Alert(Alert.AlertType.WARNING, "Vehicle not found").show();
                }

                String customerSql = "SELECT * FROM customer WHERE customer_ID = ?";
                PreparedStatement customerPstmt = con.prepareStatement(customerSql);
                customerPstmt.setString(1, customerId);
                ResultSet customerResultSet = customerPstmt.executeQuery();

                if (customerResultSet.next()) {
                    txtCustomerName.setText(customerResultSet.getString("customer_Name"));
                    txtAdvancePayment.setText(customerResultSet.getString("vehicle_AdvancePayment"));
                    txtDepositAmount.setText(customerResultSet.getString("vehicle_RefundableDeposit"));
                } else {
                    new Alert(Alert.AlertType.WARNING, "Customer not found").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void saveCustomerRentData() {
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        String vehicleId = txtVehicleId.getText();
        String vehicleNumber = txtVehicleNumber.getText();
        String vehicleBrand = txtVehicleBrand.getText();
        String vehicleModel = txtVehicleModel.getText();
        String vehicleName = txtVehicleName.getText();
        double vehicleRent = Double.parseDouble(txtVehicleRent.getText());
        int numberOfRentDays = Integer.parseInt(txtNumberOfRentDays.getText());
        double totalPayment = Double.parseDouble(txtTotalRent.getText());
        double vehicleRefundableDeposit = Double.parseDouble(txtDepositAmount.getText());
        double vehicleAdvancePayment = Double.parseDouble(txtAdvancePayment.getText());

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String insertSql = "INSERT INTO customerRent (customer_ID, customer_Name, vehicle_Id, " +
                    "vehicle_Number, vehicle_Brand, vehicle_Model, vehicle_Name, vehicle_Rent, " +
                    "NumberOfRentDays, total, vehicle_RefundableDeposit, vehicle_AdvancePayment) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertSql);
            pstmt.setString(1, customerId);
            pstmt.setString(2, customerName);
            pstmt.setString(3, vehicleId);
            pstmt.setString(4, vehicleNumber);
            pstmt.setString(5, vehicleBrand);
            pstmt.setString(6, vehicleModel);
            pstmt.setString(7, vehicleName);
            pstmt.setDouble(8, vehicleRent);
            pstmt.setInt(9, numberOfRentDays);
            pstmt.setDouble(10, totalPayment);
            pstmt.setDouble(11, vehicleRefundableDeposit);
            pstmt.setDouble(12, vehicleAdvancePayment);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Data saved to customerRent table").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save data").show();
            }

            pstmt.close();
            con.close();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtVehicleId.clear();
        txtVehicleNumber.clear();
        txtVehicleBrand.clear();
        txtVehicleModel.clear();
        txtVehicleName.clear();
        txtVehicleRent.clear();
        txtNumberOfRentDays.clear();
        txtTotalRent.clear();
        txtDepositAmount.clear();
        txtAdvancePayment.clear();
    }
}
