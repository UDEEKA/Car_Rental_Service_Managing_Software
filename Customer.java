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

public class Customer {
    private String customerId;
    private String customerNic;
    private String customerName;
    private String customerAddress;
    private String customerProvince;
    private String customerPhoneNumber;


}
