package com.demo.store.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ResponseMockito {

    private  String cause;
    private String[] stackTrace;
    private String message;
    private String localizedMessage;

}
