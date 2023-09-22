package lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter

public class Car {
    private String carId;
    private String carNumber;
    private String carBrand;
    private String carModel;
    private String carName;
    private int carYear;
    private double carRent;
    private double carDeposit;
    private double carAdvancePayment;
    private int status;

}
