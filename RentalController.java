package lk.ijse.carRentalServiceCmjd102Udeeka.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.Rental;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalController {
    public AnchorPane overdueRentalNode;
    @FXML
    private TableView<Rental> overdueRentalsTable;

    @FXML
    private TableColumn<Rental, String> rentalIdColumn;

    @FXML
    private TableColumn<Rental, String> customerIdColumn;

    @FXML
    private TableColumn<Rental, String> vehicleIdColumn;

    @FXML
    private TableColumn<Rental, LocalDate> returnDateColumn;

    public void initialize() {
        rentalIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle_Id"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        buttonloadOverdueRentalsOnAction();
    }

    @FXML
    private void buttonloadOverdueRentalsOnAction() {
        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM rentCarForm WHERE endDate < ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));

            ResultSet resultSet = pstmt.executeQuery();

            List<Rental> overdueRentals = new ArrayList<>();

            while (resultSet.next()) {
                Rental rental = new Rental();
                rental.setRentalId(resultSet.getString("id"));
                rental.setCustomerId(resultSet.getString("customer_ID"));
                rental.setVehicleId(resultSet.getString("vehicle_Id"));
                rental.setReturnDate(resultSet.getDate("endDate").toLocalDate());

                overdueRentals.add(rental);
            }
            overdueRentalsTable.getItems().addAll(overdueRentals);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void buttonBackonAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carServiceDashboard_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.overdueRentalNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Service Dashboard");
    }
}

