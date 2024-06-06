package com.demo.store.api.model;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class ResponseRentalAgreement {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private String rentalDays;
    private String checkoutDate;
    private String dueDate;
    private String dailyRentalCharge;
    private String chargeDays;
    private String preDiscountCharge;
    private String discountPercentage;
    private String discountAmount;
    private String finalCharge;

}
