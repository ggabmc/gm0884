package com.demo.store.api;

import com.demo.store.api.model.InputRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@SpringBootTest
@AutoConfigureMockMvc
class DemoWsApplicationTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;


    public  static Stream<Arguments> rentalAgreements() {
        return Stream.of(
                arguments(new InputRequest("JAKR","09/03/2015", "5", "101")),
                arguments(new InputRequest("LADW","07/02/2020", "3", "10")),
                arguments(new InputRequest("CHNS","07/02/2015", "5", "25")),
                arguments(new InputRequest("JAKD","09/03/2015", "6", "0")),
                arguments(new InputRequest("JAKR","07/02/2015", "9", "0")),
                arguments(new InputRequest("JAKR","07/02/2020", "4", "50")),
                arguments(new InputRequest("JAKR","07/02/2020", "0", "50"))
        );
    }
    @ParameterizedTest(name = "#{index} - Rental Agreement")
    @MethodSource("rentalAgreements")
    @DisplayName("Rental Agreement")
    public void rentalAgreementTest(InputRequest inputRequest) throws Exception {
        int discountPercent = Integer.parseInt(inputRequest.getDiscountPercent());
        int rentalDays = Integer.parseInt(inputRequest.getRentalDays());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/checkout").contentType(MediaType.APPLICATION_JSON)
                .content(new  ObjectMapper().writeValueAsString(inputRequest));

        MvcResult mvcResult  =  mockMvc.perform(requestBuilder).andReturn();

        if(discountPercent < 0 || discountPercent > 100 || rentalDays == 0){

            assertEquals(400, mvcResult.getResponse().getStatus());

        }else{
            //String response = mvcResult.getResponse().getContentAsString();
            //ObjectMapper objectMapper = new ObjectMapper();
            //ResponseRentalAgreement rentalAgreement = objectMapper.readValue(response, ResponseRentalAgreement.class);
            assertEquals(200, mvcResult.getResponse().getStatus());
        }
    }
}