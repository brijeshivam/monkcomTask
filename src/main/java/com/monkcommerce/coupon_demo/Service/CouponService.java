package com.monkcommerce.coupon_demo.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkcommerce.coupon_demo.Errors.ErrorType;
import com.monkcommerce.coupon_demo.Model.Coupon;
import com.monkcommerce.coupon_demo.Repository.CouponRepository;
import com.monkcommerce.coupon_demo.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.getAllCoupons();
    }

    public Map<String, List<Number>> addCoupons(Object coupons) {
        try {
            if (coupons instanceof Map<?, ?> map) {
                List<Coupon> listOfCoupon = new ArrayList<>();
                listOfCoupon.add(new Coupon((Map<String, Object>) map));
                return Map.of("IDs", couponRepository.addCoupons(listOfCoupon));
            } else if (coupons instanceof List<?> list) {
                List<Coupon> listOfCoupon = new ArrayList<>();
                list.forEach(obj -> listOfCoupon.add(new Coupon((Map<String, Object>) obj)));
                return Map.of("IDs", couponRepository.addCoupons(listOfCoupon));
            } else throw ErrorType.BadRequest.getException("Body does not map to coupon structure.");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw ErrorType.BadRequest.getException("Body does not map to coupon structure.");
        }
    }

    public List<Map<String, Object>> applicableCoupons(Object cart) {
        if (cart instanceof Map<?, ?> map) {
            List<Map<String, Object>> items = ((Map<String, List<Map<String, Object>>>) ((Map<String, Object>) map).get("cart")).get("items");
            List<Product> productsInCart = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            items.forEach(item -> productsInCart.add(objectMapper.convertValue(item, Product.class)));
            AtomicInteger totalCartValue = new AtomicInteger();
            productsInCart.forEach(product -> totalCartValue.addAndGet(product.getValue()));
            List<Integer> productIds = new ArrayList<>();
            productsInCart.forEach(product -> productIds.add(product.getProduct_id()));

            List<Map<String,Object>> applicableCouponList = new ArrayList<>();
            applicableCouponList.addAll(couponRepository.cartWiseCoupons(totalCartValue.intValue()));
            applicableCouponList.addAll(couponRepository.productWiseCoupons(productIds.stream()
                    .mapToInt(Integer::intValue)
                    .toArray()));
            applicableCouponList.addAll(couponRepository.bxGyCoupons(productsInCart));


            return applicableCouponList;
        }
        return null;
    }

    public Map<String, Object> applyCouponsByID(String id, Object items) {
        return null;
    }

    public Object getCouponByID(String id) {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw ErrorType.NotFound.getException("ID does not exist");
        }
        return couponRepository.getCouponByID(idInt);
    }

    public Object updateCouponByID(Object coupon, String id) {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw ErrorType.NotFound.getException("ID does not exist");
        }
        try {
            if (coupon instanceof Map<?, ?> map) {
                return couponRepository.updateCouponByID(new Coupon((Map<String, Object>) map), idInt);
            } else throw ErrorType.BadRequest.getException("Body does not map to coupon structure.");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw ErrorType.BadRequest.getException("Body does not map to coupon structure.");
        }
    }

    public Object deleteCouponByID(String id) {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw ErrorType.NotFound.getException("ID does not exist");
        }
        return couponRepository.deleteCouponByID(idInt);
    }
}
