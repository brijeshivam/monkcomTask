package com.monkcommerce.coupon_demo.Errors;

import java.util.Map;

public class CustomException extends RuntimeException{

    Map<String,Object> msg;
    public CustomException(Map<String,Object> msg) {
        super(msg.get("Reason").toString());
        this.msg = msg;
    }

    public Map<String,Object> getMsg() {
        return msg;
    }
}
