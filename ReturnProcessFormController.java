package lk.ijse.carRentalServiceCmjd102Udeeka.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnProcessFormController {
    public AnchorPane returnProcessNode;
    public DatePicker dateOfReturn;
    public TextField txtDateOfRentStart;
    public TextField txtRentPerDay;

    @FXML
    private TextField txtBalancePayment;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtRefundableDeposit;

    @FXML
    private TextField txtTotalPayment;

    @FXML
    private TextField txtVehicleId;

    @FXML
    private TextField txtVehicleName;

    @FXML
    private TextField txtNumberOfRentDays;

    @FXML
    private TextField txtAdvancePayment;


    @FXML
    void buttonClearFieldsOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void buttonDoneOnAction(ActionEvent event) {
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        String vehicleId = txtVehicleId.getText();
        String vehicleName = txtVehicleName.getText();
        LocalDate returnDate = dateOfReturn.getValue();
        double rentTotal = Double.parseDouble(txtTotalPayment.getText());
        double balancePayment = Double.parseDouble(txtBalancePayment.getText());
        double refundableDeposit = Double.parseDouble(txtRefundableDeposit.getText());

        if (!returnDate.isEqual(LocalDate.now())) {
            new Alert(Alert.AlertType.ERROR, "Return date must be today's date").show();
            return;
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO returnCar VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setString(1, customerId);
            pstm.setString(2, customerName);
            pstm.setString(3, vehicleId);
            pstm.setString(4, vehicleName);
            pstm.setDate(5, Date.valueOf(returnDate));
            pstm.setDouble(6, rentTotal);
            pstm.setDouble(7, balancePayment);
            pstm.setDouble(8, refundableDeposit);

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Return Record successfully saved").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtCustomerIdSearchOnAction(ActionEvent event) {
        String customerId = txtCustomerId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT cr.customer_Name, cr.vehicle_Id, cr.vehicle_Name, cr.vehicle_RefundableDeposit, cr.vehicle_AdvancePayment, " +
                    "rc.startDate FROM customerRent cr " +
                    "JOIN rentCarForm rc ON cr.customer_ID = rc.customer_ID " +
                    "WHERE cr.customer_ID = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, customerId);
            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String customerName = resultSet.getString("customer_Name");
                String vehicleId = resultSet.getString("vehicle_Id");
                String vehicleName = resultSet.getString("vehicle_Name");
                double refundableDeposit = resultSet.getDouble("vehicle_RefundableDeposit");
                double advancePayment = resultSet.getDouble("vehicle_AdvancePayment");

                txtCustomerName.setText(customerName);
                txtVehicleId.setText(vehicleId);
                txtVehicleName.setText(vehicleName);
                txtRefundableDeposit.setText(String.valueOf(refundableDeposit));
                txtAdvancePayment.setText(String.valueOf(advancePayment));

                String rentStartDateStr = resultSet.getString("startDate");
                LocalDate rentStartDate = LocalDate.parse(rentStartDateStr); // Parse the rent start date from the database
                LocalDate currentDate = LocalDate.now();
                long daysDifference = ChronoUnit.DAYS.between(rentStartDate, currentDate);
                txtNumberOfRentDays.setText(String.valueOf(daysDifference));

                dateOfReturn.setValue(currentDate);

                txtDateOfRentStart.setText(rentStartDateStr);

            } else {
                new Alert(Alert.AlertType.WARNING, "No rental record found for this customer").show();

                txtCustomerName.clear();
                txtVehicleId.clear();
                txtVehicleName.clear();
                txtRefundableDeposit.clear();
                txtAdvancePayment.clear();
                txtNumberOfRentDays.clear();
                txtRentPerDay.clear();
                txtDateOfRentStart.clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error fetching data: " + e.getMessage()).show();

            txtCustomerName.clear();
            txtVehicleId.clear();
            txtVehicleName.clear();
            txtRefundableDeposit.clear();
            txtAdvancePayment.clear();
            txtNumberOfRentDays.clear();
            txtRentPerDay.clear();
            txtDateOfRentStart.clear();
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customerRent WHERE customer_ID = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, customerId);
            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {

                txtRentPerDay.setText(resultSet.getString("vehicle_Rent"));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtVehicleIdSearchOnAction(ActionEvent event) {
        String vehicleId = txtVehicleId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM userRent WHERE vehicle_Id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, vehicleId);
            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String customerId = resultSet.getString("customer_ID");
                String customerName = resultSet.getString("customer_Name");
                String vehicleName = resultSet.getString("vehicle_Name");

                txtCustomerId.setText(customerId);
                txtCustomerName.setText(customerName);
                txtVehicleName.setText(vehicleName);

            } else {
                new Alert(Alert.AlertType.WARNING, "Vehicle not found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customerRent WHERE vehicle_ID = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, vehicleId);
            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                double refundableDeposit = resultSet.getDouble("vehicle_RefundableDeposit");

                txtRefundableDeposit.setText(String.valueOf(refundableDeposit));

            } else {
                new Alert(Alert.AlertType.WARNING, "Vehicle not found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtVehicleId.setText("");
        txtVehicleName.setText("");
        dateOfReturn.setValue(null);
        txtTotalPayment.setText("");
        txtBalancePayment.setText("");
        txtRefundableDeposit.setText("");
        txtNumberOfRentDays.setText("");
        txtAdvancePayment.setText("");
        txtRentPerDay.setText("");
        txtDateOfRentStart.setText("");

    }

    public void buttonBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carServiceDashboard_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.returnProcessNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Service Dashboard");
    }

    @FXML
    void buttonCalculateOnAction(ActionEvent actionEvent) {
        String numberOfRentDaysText = txtNumberOfRentDays.getText();
        String advancePaymentText = txtAdvancePayment.getText();

        if (txtRentPerDay.getText().isEmpty() || numberOfRentDaysText.isEmpty() || advancePaymentText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter values in all the required fields.").show();
            return;
        }

        double rentPerDay = Double.parseDouble(txtRentPerDay.getText());
        int numberOfRentDays = Integer.parseInt(numberOfRentDaysText);
        double advancePayment = Double.parseDouble(advancePaymentText);

        double totalPayment = rentPerDay * numberOfRentDays;

        txtTotalPayment.setText(String.valueOf(totalPayment));

        double balancePayment = advancePayment - totalPayment;

        txtBalancePayment.setText(String.valueOf(balancePayment));
    }
}





