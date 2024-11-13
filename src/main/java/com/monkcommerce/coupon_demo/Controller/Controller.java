package com.monkcommerce.coupon_demo.Controller;

import com.monkcommerce.coupon_demo.Service.CouponService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class Controller {

    @Autowired
    CouponService couponService;

    @PostConstruct
    public void init() {
        System.out.println("working");

    }

    @GetMapping(value = {"/coupons/{id}", "/coupons/{id}/"}, produces = "application/json")
    public ResponseEntity<Object> getCouponByID(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCouponByID(id));
    }

    @GetMapping(value = {"/coupons", "/coupons/"}, produces = "application/json")
    public ResponseEntity<Object> getAllCoupons() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getAllCoupons());
    }

    @PostMapping(value = {"/coupons", "/coupons/"}, produces = "application/json")
    public ResponseEntity<Object> addCoupons(@RequestBody Object coupon) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponService.addCoupons(coupon));

    }

    @PutMapping(value = {"/coupons/{id}", "/coupons/{id}/"}, produces = "application/json")
    public ResponseEntity<Object> updateCouponByID(@RequestBody Object item, @PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(couponService.updateCouponByID(item, id));
    }

    @DeleteMapping(value = {"/coupons/{id}", "/coupons/{id}/"}, produces = "application/json")
    public ResponseEntity<Object> deleteCouponByID(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(couponService.deleteCouponByID(id));
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<Object> applicableCoupons(@RequestBody Object cart) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.applicableCoupons(cart));
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Object> applyCouponsByID(@RequestBody Object cart, @PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.applyCouponsByID(id, cart));
    }
}
