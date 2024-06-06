package com.demo.store.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "tools")
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Tool {

    @Id
    @Column(name = "tool_code")
    private String toolCode;

    @Column(name = "tool_type")
    private String toolType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "daily_charge")
    private double dailyCharge;

    @Column(name = "weekday_charge")
    private String IsWeekDayCharge;

    @Column(name = "weekend_charge")
    private String IsWeekendCharge;

    @Column(name = "holiday_charge")
    private String IsHolidayCharge;

}
