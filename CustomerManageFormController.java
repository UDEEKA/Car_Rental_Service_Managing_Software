package lk.ijse.carRentalServiceCmjd102Udeeka.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.db.DbConnection;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.Customer;
import lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto.CustomerTm;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerManageFormController {

    public TextField txtCustomerId;
    public TextField txtCustomerNic;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtCustomerPhoneNumber;
    public AnchorPane customerManageNode;
    public TextField txtCustomerProvince;
    public TableView tableCustomer;


    @FXML
    private TableColumn<?, ?> colCustomerProvince;

    @FXML
    private TableColumn<?, ?> colCustomerAddress;

    @FXML
    private TableColumn<?, ?> colCustomerName;

    @FXML
    private TableColumn<?, ?> colCustomerNic;

    @FXML
    private TableColumn<?, ?> colCustomerPhoneNumber;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    public void txtIdSearchOnAction(ActionEvent actionEvent) throws IOException {
        String id= txtCustomerId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM customer WHERE customer_ID = ?";

            PreparedStatement pstmId = con.prepareStatement(sql);
            pstmId.setString(1, id);


            ResultSet resultSetId = pstmId.executeQuery();


            if (resultSetId.next()) {
                String txtId = resultSetId.getString(1);
                String txtNic = resultSetId.getString(2);
                String txtName = resultSetId.getString(3);
                String txtAddress = resultSetId.getString(4);
                String txtProvince = resultSetId.getString(5);
                String txtPhoneNumber = resultSetId.getString(6);

                setFieldsId(txtId,
                        txtNic,
                        txtName,
                        txtAddress,
                        txtProvince,
                        txtPhoneNumber);
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void setFieldsId(String txtId, String txtNic, String txtName, String txtAddress,  String txtProvince, String txtPhoneNumber) throws IOException {
        this.txtCustomerId.setText(txtId);
        this.txtCustomerNic.setText(txtNic);
        this.txtCustomerName.setText(txtName);
        this.txtCustomerAddress.setText(txtAddress);
        this.txtCustomerProvince.setText(txtProvince);
        this.txtCustomerPhoneNumber.setText(txtPhoneNumber);
    }
    private void clearFields() {
        txtCustomerId.setText("");
        txtCustomerNic.setText("");
        txtCustomerName.setText("");
        txtCustomerAddress.setText("");
        txtCustomerProvince.setText("");
        txtCustomerPhoneNumber.setText("");
    }

    public void txtCustomerNicSearchOnAction(ActionEvent actionEvent) throws IOException {
        String nic = txtCustomerNic.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM customer WHERE customer_NIC = ?";

            PreparedStatement pstmNic = con.prepareStatement(sql);
            pstmNic.setString(1, nic);

            ResultSet resultSetNic = pstmNic.executeQuery();

            if (resultSetNic.next()) {
                String txtId = resultSetNic.getString(1);
                String txtNic =resultSetNic.getString(2);
                String txtName = resultSetNic.getString(3);
                String txtAddress = resultSetNic.getString(4);
                String txtProvince = resultSetNic.getString(5);
                String txtPhoneNumber = resultSetNic.getString(6);

                setFieldsNic(txtId, txtNic, txtName, txtAddress, txtProvince, txtPhoneNumber);
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void setFieldsNic(String txtId, String txtNic, String txtName, String txtAddress,  String txtProvince, String txtPhoneNumber) throws IOException {
        this.txtCustomerId.setText(txtId);
        this.txtCustomerNic.setText(txtNic);
        this.txtCustomerName.setText(txtName);
        this.txtCustomerAddress.setText(txtAddress);
        this.txtCustomerProvince.setText(txtProvince);
        this.txtCustomerPhoneNumber.setText(txtPhoneNumber);
    }

    public void txtCustomerPhoneNumberOnAction(ActionEvent actionEvent) throws IOException {
        String phoneNumber = txtCustomerPhoneNumber.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM customer WHERE  customer_Phone_Number = ?";

            PreparedStatement pstmPhoneNumber = con.prepareStatement(sql);
            pstmPhoneNumber.setString(1,phoneNumber);


            ResultSet resultSetPhoneNumber = pstmPhoneNumber.executeQuery();


            if (resultSetPhoneNumber.next()) {
                String txtId = resultSetPhoneNumber.getString(1);
                String txtNic =resultSetPhoneNumber.getString(2);
                String txtName = resultSetPhoneNumber.getString(3);
                String txtAddress = resultSetPhoneNumber.getString(4);
                String txtProvince = resultSetPhoneNumber.getString(5);
                String txtPhoneNumber =resultSetPhoneNumber.getString(6);

                setFieldsresultSetPhoneNumber(txtId, txtNic, txtName, txtAddress, txtProvince, txtPhoneNumber);
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void setFieldsresultSetPhoneNumber(String txtId, String txtNic, String txtName, String txtAddress,  String txtProvince, String txtPhoneNumber) throws IOException {
        this.txtCustomerId.setText(txtId);
        this.txtCustomerNic.setText(txtNic);
        this.txtCustomerName.setText(txtName);
        this.txtCustomerAddress.setText(txtAddress);
        this.txtCustomerProvince.setText(txtProvince);
        this.txtCustomerPhoneNumber.setText(txtPhoneNumber);
    }

    public void buttoCustomerBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/carServiceDashboard_form.fxml"));

        Scene scene = new Scene(root);

        Stage stage = (Stage) this.customerManageNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Car Service Dashboard");
    }

    public void buttonCustomerUpdateOnAction(ActionEvent actionEvent) {
        String id = txtCustomerId.getText();
        String nic = txtCustomerNic.getText();
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();
        String province = txtCustomerProvince.getText();
        String phoneNumber = txtCustomerPhoneNumber.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "UPDATE customer SET customer_NIC = ?,customer_Name = ?," +
                    "customer_Address = ?, " +
                    "customer_Province = ?, customer_Phone_Number = ? WHERE customer_ID = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1,nic);
            pstm.setString(2,name);
            pstm.setString(3, address);
            pstm.setString(4, province);
            pstm.setString(5, phoneNumber);
            pstm.setString(6, id);

            boolean isUpdated = pstm.executeUpdate() > 0;
            if(isUpdated){
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION,"Customer successfully updated").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void buttonCustomerSaveOnAction(ActionEvent actionEvent) {
        String id = txtCustomerId.getText();
        String nic = txtCustomerNic.getText();
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();
        String province = txtCustomerProvince.getText();
        String phoneNumber = txtCustomerPhoneNumber.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO customer VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setString(1, id);
            pstm.setString(2, nic);
            pstm.setString(3, name);
            pstm.setString(4, address);
            pstm.setString(5, province);
            pstm.setString(6, phoneNumber);

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Customer successfully saved").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void buttonCustomerClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void buttoCustomerDeleteOnAction(ActionEvent actionEvent) {
        String id = txtCustomerId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "DELETE FROM customer WHERE customer_ID = ?" ;

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1,id);

            boolean isDeleted = pstm.executeUpdate() > 0;
            if(isDeleted){
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION,"Customer successfully deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    public void initialize() throws SQLException {
        setCellValueFactory();
        List<Customer> customerList = loadAllCustomers();

        setTableData(customerList);
    }

    private void setTableData(List<Customer> customerList) {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        for(Customer customer : customerList) {
            var customertm = new CustomerTm(
                    customer.getCustomerId(),
                    customer.getCustomerNic(),
                    customer.getCustomerName(),
                    customer.getCustomerAddress(),
                    customer.getCustomerProvince(),
                    customer.getCustomerPhoneNumber());
            obList.add(customertm);
        }
        tableCustomer.setItems(obList);
    }

    private void setCellValueFactory() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colCustomerNic.setCellValueFactory(new PropertyValueFactory<>("Nic"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colCustomerProvince.setCellValueFactory(new PropertyValueFactory<>("Province"));
        colCustomerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
    }

    private List<Customer> loadAllCustomers() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM customer";
        Statement stm = con.createStatement();

        ResultSet resultSet = stm.executeQuery(sql);

        List<Customer> customerList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String nic = resultSet.getString(2);
            String name = resultSet.getString(3);
            String address = resultSet.getString(4);
            String province = resultSet.getString(5);
            String phoneNumber = resultSet.getString(6);

            var customer = new Customer(id, nic, name, address, province,phoneNumber);
            customerList.add(customer);
        }
        return customerList;
    }
}
