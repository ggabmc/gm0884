package com.demo.store.api.model;

import lombok.Data;

@Data
public class ExceptionMessage extends Throwable {

    private String message;

    public ExceptionMessage(String message) {
        this.message = message;
    }
}

