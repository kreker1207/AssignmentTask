package com.example.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundByIdException extends IllegalArgumentException{
    public EntityNotFoundByIdException(String s) {
        super(s);
    }
}
