package com.monkcommerce.coupon_demo.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        if(ex instanceof CustomException ce){
            return new ResponseEntity<>(ce.getMsg(), (HttpStatusCode) ce.getMsg().get("Type"));
        }else {
            Map<String, Object> message = new HashMap<>();
            message.put("Code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.put("Type", "Internal Error");
            message.put("Reason", ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}