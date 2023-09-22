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
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.Car;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class CarManageFormController {
    public TableColumn colVehicleUpdateDelete;
    @FXML
    private TableColumn<Car, Double> colVehicleDeposit;
    @FXML
    private TableColumn<Car, Double> colVehicleAdvancePayment;

    @FXML
    private AnchorPane carManageNode;

    @FXML
    private TableView<Car> tblCar;

    @FXML
    private TableColumn<Car, String> colVehicleId;

    @FXML
    private TableColumn<Car, String> colVehicleNumber;

    @FXML
    private TableColumn<Car, String> colVehicleBrand;

    @FXML
    private TableColumn<Car, String> colVehicleModel;

    @FXML
    private TableColumn<Car, String> colVehicleName;

    @FXML
    private TableColumn<Car, Integer> colVehicleYear;

    @FXML
    private TableColumn<Car, Double> colVehicleRent;

    public void initialize() throws SQLException {
        setCellValueFactory();
        List<Car> carList = loadAllCars();
        setTableData(carList);

        addUpdateAndDeleteButtonsToTable();
    }

    private void addUpdateAndDeleteButtonsToTable() {
        TableColumn<Car, Void> colVehicleUpdateDelete = new TableColumn<>("Actions");
        colVehicleUpdateDelete.setSortable(false);

        Callback<TableColumn<Car, Void>, TableCell<Car, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Car, Void> call(final TableColumn<Car, Void> param) {
                return new TableCell<>() {
                    private final HBox container = new HBox(5);
                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");

                    {
                        updateButton.setOnAction(event -> {
                            Car car = getTableView().getItems().get(getIndex());
                            try {

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateCar_form.fxml"));
                                Parent root = loader.load();

                                UpdateCarFormController updateCarFormController = loader.getController();

                                updateCarFormController.initData(car);

                                Stage updateCarStage = new Stage();
                                updateCarStage.setTitle("Update Car");
                                updateCarStage.setScene(new Scene(root));

                                updateCarStage.initModality(Modality.WINDOW_MODAL);
                                updateCarStage.initOwner(carManageNode.getScene().getWindow());
                                updateCarStage.showAndWait();

                                List<Car> updatedCarList = loadAllCars();
                                setTableData(updatedCarList);
                            } catch (IOException e) {
                                new Alert(Alert.AlertType.ERROR, "Error loading update car form").show();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });


                        deleteButton.setOnAction(event -> {
                            Car car = getTableView().getItems().get(getIndex());

                            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationDialog.setTitle("Confirm Deletion");
                            confirmationDialog.setHeaderText("Confirm Deletion");
                            confirmationDialog.setContentText("Are you sure you want to delete this car?");
                            ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                            ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                            confirmationDialog.getButtonTypes().setAll(confirmButton, cancelButton);

                            Optional<ButtonType> result = confirmationDialog.showAndWait();

                            if (result.isPresent() && result.get() == confirmButton) {

                                String id = car.getCarId();

                                try {
                                    Connection con = DbConnection.getInstance().getConnection();

                                    String deleteRentCarFormSql = "DELETE FROM rentcarform WHERE vehicle_Id = ?";
                                    PreparedStatement deleteRentCarFormPstm = con.prepareStatement(deleteRentCarFormSql);
                                    deleteRentCarFormPstm.setString(1, id);
                                    boolean isRentCarFormsDeleted = deleteRentCarFormPstm.executeUpdate() > 0;

                                    if (isRentCarFormsDeleted) {

                                        String deleteCarSql = "DELETE FROM car WHERE Vehicle_Id = ?";
                                        PreparedStatement deleteCarPstm = con.prepareStatement(deleteCarSql);
                                        deleteCarPstm.setString(1, id);
                                        boolean isDeleted = deleteCarPstm.executeUpdate() > 0;

                                        if (isDeleted) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Car successfully deleted").show();

                                            tblCar.getItems().remove(car);
                                        }
                                    } else {
                                        new Alert(Alert.AlertType.ERROR, "Error deleting related rentcarform records").show();
                                    }
                                } catch (SQLException e) {
                                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                                }
                            }
                        });

                        container.getChildren().addAll(updateButton, deleteButton);
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(container);
                        }
                    }
                };
            }
        };

        colVehicleUpdateDelete.setCellFactory(cellFactory);
        tblCar.getColumns().add(colVehicleUpdateDelete);
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

            Car car = new Car(id, number, brand, model, name, year, rent, deposit,advancePayment, carStatus);
            carList.add(car);
        }

        return carList;
    }
    private void setCellValueFactory() {
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        colVehicleNumber.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        colVehicleBrand.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
        colVehicleModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("carName"));
        colVehicleYear.setCellValueFactory(new PropertyValueFactory<>("carYear"));
        colVehicleRent.setCellValueFactory(new PropertyValueFactory<>("carRent"));
        colVehicleDeposit.setCellValueFactory(new PropertyValueFactory<>("carDeposit"));
        colVehicleAdvancePayment.setCellValueFactory(new PropertyValueFactory<>("carAdvancePayment"));
    }
    private void setTableData(List<Car> carList) {
        ObservableList<Car> obList = FXCollections.observableArrayList(carList);
        tblCar.setItems(obList);
    }

    public void btnAddNewCarOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/carAdd_form.fxml"));
        carManageNode.getChildren().clear();
        carManageNode.getChildren().add(root);
    }

    public void buttonBackCarManageOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/carServiceDashboard_form.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) carManageNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Car Service Dashboard");
    }

    public void buttonUpdateCarManageOnAction(ActionEvent actionEvent) {

    }

    public void buttonDeleteCarManageOnAction(ActionEvent actionEvent) {
    }
}
