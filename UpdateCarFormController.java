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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCarFormController {

    public TextField txtVehicleAvailability;
    @FXML
    private TextField txtVehicleAdvancePayment;

    @FXML
    private TextField txtVehicleBrand;

    @FXML
    private TextField txtVehicleId;

    @FXML
    private TextField txtVehicleModel;

    @FXML
    private TextField txtVehicleName;

    @FXML
    private TextField txtVehicleNumber;

    @FXML
    private TextField txtVehicleRefundableDeposit;

    @FXML
    private TextField txtVehicleRent;

    @FXML
    private TextField txtVehicleYear;

    @FXML
    private AnchorPane updateVehicleNode;

    @FXML
    void buttonClearFieldsOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void buttonSaveOnAction(ActionEvent event) {
        String id = txtVehicleId.getText();
        String number = txtVehicleNumber.getText();
        String brand = txtVehicleBrand.getText();
        String model = txtVehicleModel.getText();
        String name = txtVehicleName.getText();
        int year = Integer.parseInt(txtVehicleYear.getText());
        double rent = Double.parseDouble(txtVehicleRent.getText());
        double deposit = Double.parseDouble(txtVehicleRefundableDeposit.getText());
        double advance = Double.parseDouble(txtVehicleAdvancePayment.getText());
        int availability = Integer.parseInt(txtVehicleAvailability.getText());

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "UPDATE car SET vehicle_Id = ?," +
                    "vehicle_Number = ?," +
                    "vehicle_Brand = ?, " +
                    "vehicle_Model = ?, " +
                    "vehicle_Name = ?, " +
                    "vehicle_Year = ?, " +
                    "vehicle_Rent = ?, " +
                    "vehicle_RefundableDeposit = ?," +
                    " vehicle_AdvancePayment = ?, " +
                    "status = ?  WHERE vehicle_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1,id);
            pstm.setString(2,number);
            pstm.setString(3, brand);
            pstm.setString(4, model);
            pstm.setString(5, name);
            pstm.setString(6, String.valueOf(year));
            pstm.setString(7, String.valueOf(rent));
            pstm.setString(8, String.valueOf(deposit));
            pstm.setString(9, String.valueOf(advance));
            pstm.setString(10, String.valueOf(availability));

            boolean isUpdated = pstm.executeUpdate() > 0;
            if(isUpdated){
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION,"Vehicle Details Successfully updated").show();
            }
        } catch (
                SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    public void txtVehicleIdSearchOnAction(ActionEvent actionEvent) {
        String id = txtVehicleId.getText().trim();

        if (!id.isEmpty()) {
            try {
                Connection con = DbConnection.getInstance().getConnection();
                String sql = "SELECT * FROM car WHERE vehicle_Id = ?";
                PreparedStatement pstmId = con.prepareStatement(sql);
                pstmId.setString(1, id);

                ResultSet resultSetId = pstmId.executeQuery();

                if (resultSetId.next()) {
                    String txtNumber = resultSetId.getString(2);
                    String txtBrand = resultSetId.getString(3);
                    String txtModel = resultSetId.getString(4);
                    String txtName = resultSetId.getString(5);
                    int txtYear = resultSetId.getInt(6);
                    double txtRent = resultSetId.getDouble(7);
                    double txtDeposit = resultSetId.getDouble(8);
                    double txtAdvancePayment = resultSetId.getDouble(9);
                    int txtStatus = resultSetId.getInt(10);

                    setFieldsId(id,
                            txtNumber,
                            txtBrand,
                            txtModel,
                            txtName,
                            txtYear,
                            txtRent,
                            txtDeposit,
                            txtAdvancePayment,
                            txtStatus);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Vehicle not found").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid Vehicle ID").show();
        }
    }
    private void setFieldsId(String txtId, String txtNumber, String txtBrand, String txtModel,
                             String txtName, int txtYear, double txtRent, double txtDeposit,
                             double txtAdvancePayment, int txtStatus) {
        this.txtVehicleId.setText(txtId);
        this.txtVehicleNumber.setText(txtNumber);
        this.txtVehicleBrand.setText(txtBrand);
        this.txtVehicleModel.setText(txtModel);
        this.txtVehicleName.setText(txtName);
        this.txtVehicleYear.setText(String.valueOf(txtYear));
        this.txtVehicleRent.setText(String.valueOf(txtRent));
        this.txtVehicleRefundableDeposit.setText(String.valueOf(txtDeposit));
        this.txtVehicleAdvancePayment.setText(String.valueOf(txtAdvancePayment));
        this.txtVehicleAvailability.setText(String.valueOf(txtStatus));
    }

    private void clearFields() {
        txtVehicleId.setText("");
        txtVehicleNumber.setText("");
        txtVehicleBrand.setText("");
        txtVehicleModel.setText("");
        txtVehicleName.setText("");
        txtVehicleYear.setText("");
        txtVehicleRent.setText("");
        txtVehicleRefundableDeposit.setText("");
        txtVehicleAdvancePayment.setText("");
        txtVehicleAvailability.setText("");
    }

    public void buttonBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carManage_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.updateVehicleNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Service Dashboard");
    }
    public void initData(Car car) {
    }
}







