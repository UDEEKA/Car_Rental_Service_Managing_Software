package lk.ijse.carRentalServiceCmjd102Udeeka.controller.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Rental {
    private String rentalId;
    private String customerId;
    private String vehicleId;
    private LocalDate returnDate;

}

