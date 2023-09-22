package lk.ijse.carRentalServiceCmjd102Udeeka.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.Car;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.CarTm;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RentCarFormController {


    public Button buttonNext;
    public Button buttonSelect;
    public TableColumn colVehicleRefundableDeposit;
    public TableColumn colVehicleAdvancePayment;
    public TableColumn colVehicleStatus;
    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtRentPerDay;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtSelectVehicleId;

    @FXML
    private TextField txtVehicleId;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker returnDatePicker;

    @FXML
    private TableView<CarTm> tableCar;

    @FXML
    private TableColumn<CarTm, String> colVehicleId;

    @FXML
    private TableColumn<CarTm, String> colVehicleBrand;

    @FXML
    private TableColumn<CarTm, String> colVehicleModel;

    @FXML
    private TableColumn<CarTm, String> colVehicleName;

    @FXML
    private TableColumn<CarTm, String> colVehicleNumber;

    @FXML
    private TableColumn<CarTm, String> colVehicleRent;

    @FXML
    private TableColumn<CarTm, String> colVehicleYear;

    @FXML
    private AnchorPane customerCarRentManageNode;

    private boolean hasActiveRental(String customerId) {
        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM rentcarform WHERE customer_ID = ? AND endDate >= ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, customerId);
            pstmt.setString(2, String.valueOf(LocalDate.now()));

            ResultSet resultSet = pstmt.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean isCarAvailable(String vehicleId, LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM rentCarForm WHERE vehicle_Id = ? AND " +
                    "((startDate >= ? AND startDate <= ?) OR " +
                    "(endDate >= ? AND endDate <= ?))";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vehicleId);
            pstmt.setString(2, String.valueOf(startDate));
            pstmt.setString(3, String.valueOf(endDate));
            pstmt.setString(4, String.valueOf(startDate));
            pstmt.setString(5, String.valueOf(endDate));

            ResultSet resultSet = pstmt.executeQuery();

            return !resultSet.next(); // Car is available if there are no overlapping bookings

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately
        }
    }

    public void buttonSelectOnAction(ActionEvent actionEvent) {

        String customerId = txtCustomerId.getText();

        if (hasActiveRental(customerId)) {

            new Alert(Alert.AlertType.WARNING, "Please return your previous rental before renting another car.").show();
            return;
        }

        String vehicleId = txtSelectVehicleId.getText();

        if (!isCarAvailable(vehicleId, startDatePicker.getValue(), returnDatePicker.getValue())) {
            new Alert(Alert.AlertType.WARNING, "Selected car is not available for the specified date range.").show();
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate returnDate = returnDatePicker.getValue();
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            new Alert(Alert.AlertType.ERROR, "Please Enter a Valid Date").show();
            return;
        }

        long rentalDuration = startDate.until(returnDate, ChronoUnit.DAYS);

        if (rentalDuration <= 0 || rentalDuration > 30) {
            new Alert(Alert.AlertType.WARNING, "Rental duration must be between 1 and 30 days.").show();
            return;
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Car rented successfully for " + rentalDuration + " days.").show();
        }

        txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        LocalDate rentalStartDate = startDatePicker.getValue(); // Get the rental start date from DatePicker
        LocalDate rentalEndDate = returnDatePicker.getValue(); // Get the rental end date from DatePicker
        txtSelectVehicleId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO rentCarForm (customer_ID, customer_Name, startDate, endDate, vehicle_Id, NumberOfRentDays) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, customerId);
            pstm.setString(2, customerName);
            pstm.setString(3, String.valueOf(rentalStartDate));
            pstm.setString(4, String.valueOf(rentalEndDate));
            pstm.setString(5, vehicleId);
            pstm.setString(6, String.valueOf(rentalDuration));

            boolean isSaved = pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtCustomerIdOnAction(ActionEvent actionEvent) {
        String id = txtCustomerId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customer WHERE customer_ID = ?";
            PreparedStatement pstmId = con.prepareStatement(sql);
            pstmId.setString(1, id);

            ResultSet resultSetId = pstmId.executeQuery();

            if (resultSetId.next()) {
                String txtName = resultSetId.getString(3);
                setFields(resultSetId.getString(1), txtName);
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setFields(String id, String txtName) {
        txtCustomerId.setText(id);
        txtCustomerName.setText(txtName);
    }

    @FXML
    public void initialize() {
        startDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(1));

        try {
            setCellValueFactory();
            List<Car> carList = loadAllCars();
            setTableData(carList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableCar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleCarSelection(newValue);
            }
        });
    }

    private void setTableData(List<Car> carList) {
        ObservableList<CarTm> obList = FXCollections.observableArrayList();

        for (Car car : carList) {
            var tm = new CarTm(car.getCarId(),
                    car.getCarNumber(),
                    car.getCarBrand(),
                    car.getCarModel(),
                    car.getCarName(),
                    car.getCarYear(),
                    car.getCarRent(),
                    car.getCarDeposit(),
                    car.getCarAdvancePayment(),
                    car.getStatus());
            obList.add(tm);
        }
        tableCar.setItems(obList);
    }

    private void setCellValueFactory() {

            colVehicleId.setCellValueFactory(new PropertyValueFactory<>("carId"));
            colVehicleNumber.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
            colVehicleBrand.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
            colVehicleModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
            colVehicleName.setCellValueFactory(new PropertyValueFactory<>("carName"));
            colVehicleYear.setCellValueFactory(new PropertyValueFactory<>("carYear"));
            colVehicleRent.setCellValueFactory(new PropertyValueFactory<>("carRent"));
            colVehicleRefundableDeposit.setCellValueFactory(new PropertyValueFactory<>("carDeposit"));
            colVehicleAdvancePayment.setCellValueFactory(new PropertyValueFactory<>("carAdvancePayment"));
            colVehicleStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        }

    private List<Car> loadAllCars() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM car";
        Statement stm = con.createStatement();
        ResultSet resultSet = stm.executeQuery(sql);
        List<Car> carList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String number = resultSet.getString(2);
            String brand = resultSet.getString(3);
            String model = resultSet.getString(4);
            String name = resultSet.getString(5);
            int year = resultSet.getInt(6);
            double rent = resultSet.getDouble(7);
            double deposit = resultSet.getDouble(8);
            double advancePayment = resultSet.getDouble(9);
            int carStatus = resultSet.getInt(10);

            var car = new Car(id,
                    number, brand, model, name,
                    year, rent, deposit, advancePayment, carStatus);
            carList.add(car);
        }
        return carList;
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        if (txtVehicleId != null) {
            txtVehicleId.setText("");
        }
    }

    private void handleCarSelection(CarTm selectedCar) {
        String selectedVehicleId = selectedCar.getCarId();
        txtSelectVehicleId.setText(selectedVehicleId);
    }

    public void buttonNextOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/customerRent_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.customerCarRentManageNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Customer Rent Form");
    }

    public void buttonBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carServiceDashboard_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.customerCarRentManageNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Service Dashboard");
    }

}
