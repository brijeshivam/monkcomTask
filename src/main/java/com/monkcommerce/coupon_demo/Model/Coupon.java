package com.monkcommerce.coupon_demo.Model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Coupon {
    private int coupon_id;
    private String type;
    private Map<String,Object> details;

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
    public Coupon(Map<String,Object> map){
        ObjectMapper objectMapper = new ObjectMapper();
         Coupon coupon = objectMapper.convertValue(map, Coupon.class);
         this.type = coupon.getType();
         this.coupon_id = coupon.getCoupon_id();
         this.details = coupon.getDetails();
    }
    public Coupon(){

    }
}
