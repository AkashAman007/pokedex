package com.truelayer.pokedex.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends Exception {

    private Integer code;

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
