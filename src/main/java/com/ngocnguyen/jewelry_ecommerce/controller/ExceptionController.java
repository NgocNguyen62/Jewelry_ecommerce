package com.ngocnguyen.jewelry_ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NullPointerException.class)
    public String nullPointer() {
        return "/error/null-pointer";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerFound() {
        return "/error/404";
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest() {
        return "/error/400";
    }

    @ExceptionHandler(HttpClientErrorException.NotAcceptable.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDenied() {
        return "error/403";
    }
}

