package com.demo.store.api.model;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class InputRequest {

    private String toolCode;
    private String checkoutDate;
    private String rentalDays;
    private String discountPercent;

}
