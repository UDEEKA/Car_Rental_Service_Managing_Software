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

public class CustomerTm {
    private String id;
    private String nic;
    private String name;
    private String address;
    private String province;
    private String phoneNumber;


}
