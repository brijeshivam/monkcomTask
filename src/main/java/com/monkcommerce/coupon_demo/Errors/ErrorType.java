package com.monkcommerce.coupon_demo.Errors;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum ErrorType {
    NotAcceptable(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE, "Request not acceptable."),
    BadRequest(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Invalid request."),
    NotFound(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "Resource Not Found.");

    private final int code;
    private final HttpStatus type;
    private final String defaultReason;

    ErrorType(int code, HttpStatus type, String defaultReason) {
        this.code = code;
        this.type = type;
        this.defaultReason = defaultReason;
    }

    private Map<String,Object> getMsg(String reason) {

        Map<String, Object> message = new HashMap<>();
        message.put("Code", code);
        message.put("Type", type);
        message.put("Reason", reason != null ? reason : defaultReason);
        return message;
    }
    public CustomException getException(String reason){
        return new CustomException(this.getMsg(reason));
    }
}
