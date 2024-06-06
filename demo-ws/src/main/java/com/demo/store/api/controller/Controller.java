package com.demo.store.api.controller;

import com.demo.store.api.entity.Tool;
import com.demo.store.api.model.ExceptionMessage;
import com.demo.store.api.model.InputRequest;
import com.demo.store.api.model.ResponseRentalAgreement;
import com.demo.store.api.repos.ToolRepository;
import com.demo.store.api.service.IToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private IToolService toolService;

    @PostMapping("/checkout")
    public ResponseEntity<ResponseRentalAgreement> checkOutRental(@RequestBody InputRequest inputRequest) {

        Tool tool = toolService.getToolByToolCode(inputRequest.getToolCode());

        int rentalDays = Integer.parseInt(inputRequest.getRentalDays());

        int discountPercent =  Integer.parseInt(inputRequest.getDiscountPercent());

        if(rentalDays == 0){
            return new ResponseEntity(new ExceptionMessage(": : : Rental days must be more than 1 : : :"), HttpStatus.BAD_REQUEST);
        }

        if(discountPercent < 0 || discountPercent > 100){
            return new ResponseEntity(new ExceptionMessage(": : : Discount percentage must be between 0 to 100 range : : :"), HttpStatus.BAD_REQUEST);
        }

        LocalDate checkoutDate = LocalDate.parse(inputRequest.getCheckoutDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")); // day that tool is rented

        LocalDate dueDate = checkoutDate.plusDays(rentalDays -1);   // day to take the tool rental back

        int checkoutDay = checkoutDate.getDayOfMonth();

        int dueDay = dueDate.getDayOfMonth();

        int weekdaysNoCharge = 0;
        int weekendaysNoCharge = 0;
        int holidaysNoCharge = 0;

        List<DayOfWeek> weekdaysList = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

        for(int day = checkoutDay; day <= dueDay; day++){
            LocalDate date = LocalDate.of(checkoutDate.getYear(), checkoutDate.getMonth(), day);
            weekdaysNoCharge = weekdaysList.contains(date.getDayOfWeek()) && tool.getIsWeekDayCharge().equals("NO") ? weekdaysNoCharge + 1 : weekdaysNoCharge;
            weekendaysNoCharge = date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && tool.getIsWeekendCharge().equals("NO") ? weekendaysNoCharge + 1 : weekendaysNoCharge;
            weekendaysNoCharge = date.getDayOfWeek().equals(DayOfWeek.SUNDAY) && tool.getIsWeekendCharge().equals("NO") ? weekendaysNoCharge + 1  : weekendaysNoCharge;
            holidaysNoCharge = isIndependenceDayHoliday(date) && tool.getIsHolidayCharge().equals("NO") ? holidaysNoCharge + 1 : holidaysNoCharge;    // July 5
            holidaysNoCharge = isLaborDayHoliday(date) && tool.getIsHolidayCharge().equals("NO")  ? holidaysNoCharge + 1: holidaysNoCharge;    // First monday of september
            //System.out.println(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))+" : : : "+ date.getMonth().name()+ " "+date.getDayOfMonth() + " "+ date.getDayOfWeek().name()+ " "+ date.getYear()+ " : : :  "+ toolDescription.toString());
        }

        int chargeDays = rentalDays - (weekdaysNoCharge + weekendaysNoCharge + holidaysNoCharge);
        //double preDiscountCharge = Math.round(chargeDays * tool.getDailyCharge());    //Pre-discount charge - Calculated as charge days X daily charge. Resulting total rounded half up to cents.
        //double discountAmount = Math.round(getCalculationTotalWithDiscount(tool.getDailyCharge(), chargeDays, discountPercent));
        double preDiscountCharge = chargeDays * tool.getDailyCharge(); //  Math.round(chargeDays * tool.getDailyCharge())
        double discountAmount = getCalculationTotalWithDiscount(tool.getDailyCharge(), chargeDays, discountPercent); //Math.round()

        DecimalFormat formatter = new DecimalFormat("$#,###.##");

        // Rental Agreement Values:
        ResponseRentalAgreement rentalAgreement = new ResponseRentalAgreement();
        rentalAgreement.setToolCode(inputRequest.getToolCode());
        rentalAgreement.setToolType(tool.getToolType());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setRentalDays(String.valueOf(rentalDays));
        rentalAgreement.setCheckoutDate(inputRequest.getCheckoutDate());
        rentalAgreement.setDueDate(dueDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        rentalAgreement.setDailyRentalCharge(formatter.format(tool.getDailyCharge()));
        rentalAgreement.setChargeDays(String.valueOf(chargeDays));
        rentalAgreement.setPreDiscountCharge(formatter.format(preDiscountCharge));
        rentalAgreement.setDiscountPercentage("%"+discountPercent);
        rentalAgreement.setDiscountAmount(formatter.format(discountAmount));
        rentalAgreement.setFinalCharge(formatter.format(preDiscountCharge - discountAmount));

        printRentalAgreement(rentalAgreement);

        return ResponseEntity.status(HttpStatus.OK).body(rentalAgreement);
    }


    private void printRentalAgreement(ResponseRentalAgreement rentalAgreement){
        StringBuilder stringBuilder = new StringBuilder()
                .append("\n")
                .append(": : : Rental Agreement : : : ").append("\n")
                .append("\n")
                .append("Tool code: ").append(rentalAgreement.getToolCode()).append("\n")
                .append("Tool type: ").append(rentalAgreement.getToolType()).append("\n")
                .append("Tool brand: ").append(rentalAgreement.getToolBrand()).append("\n")
                .append("Rental days: ").append(rentalAgreement.getRentalDays()).append("\n")
                .append("Checkout date: ").append(rentalAgreement.getCheckoutDate()).append("\n")
                .append("Due date: ").append(rentalAgreement.getDueDate()).append("\n")
                .append("Daily rental charge: ").append(rentalAgreement.getDailyRentalCharge()).append("\n")
                .append("Charge days: ").append(rentalAgreement.getChargeDays()).append("\n")
                .append("Pre discount charge: ").append(rentalAgreement.getPreDiscountCharge()).append("\n")
                .append("Discount percent: ").append(rentalAgreement.getDiscountPercentage()).append("\n")
                .append("Discount amount: ").append(rentalAgreement.getDiscountAmount()).append("\n")
                .append("Final charge: ").append(rentalAgreement.getFinalCharge()).append("\n")
                .append("\n");

        System.out.println(stringBuilder.toString());
    }

    private double getCalculationTotalWithDiscount(double rentPerDay , int rentalDays, double discountPercent){
        if(discountPercent == 0){
            return 0;
        }
        double rental = rentPerDay * rentalDays;

        return  discountPercent > 0 ?  (discountPercent * rental) / 100 : rental;
    }

    private boolean isLaborDayHoliday(LocalDate checkoutDate){
        boolean isLaborDay = false;

        if(checkoutDate.getMonth().equals(Month.SEPTEMBER)){
            int dayOfMonth = 0;
            LocalDate september = LocalDate.of(checkoutDate.getYear(), Month.SEPTEMBER,1);

            for(int a =0; a< september.lengthOfMonth(); a++ ){
                if(september.getDayOfWeek().equals(DayOfWeek.MONDAY)){  // labor day
                    dayOfMonth = september.getDayOfMonth();
                    break;
                }else{
                    september = september.plusDays(1);
                }
            }

            if(checkoutDate.getDayOfWeek().equals(DayOfWeek.MONDAY) && checkoutDate.getDayOfMonth() == dayOfMonth) {
                isLaborDay = true;
            }
        }
        return  isLaborDay;
    }

    private boolean isIndependenceDayHoliday(LocalDate checkoutDate){
        boolean isIndependenceDay = false;
        if(checkoutDate.getMonth().equals(Month.JULY) && checkoutDate.getDayOfMonth() == 4){
            //DayOfWeek nameDay = checkoutDate.getDayOfWeek();

            //if(nameDay.equals(DayOfWeek.SATURDAY) || nameDay.equals(DayOfWeek.SUNDAY)){
                isIndependenceDay = true;
            //}
        }
        return  isIndependenceDay;
    }
}