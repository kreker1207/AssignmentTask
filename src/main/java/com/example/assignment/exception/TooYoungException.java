package com.example.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TooYoungException extends IllegalArgumentException{
    public TooYoungException(String s) {
        super(s);
    }
}
