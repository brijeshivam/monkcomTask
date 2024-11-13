package com.monkcommerce.coupon_demo.Repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkcommerce.coupon_demo.Errors.ErrorType;
import com.monkcommerce.coupon_demo.Model.Coupon;
import com.monkcommerce.coupon_demo.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CouponRepository {

    private final JdbcTemplate jdbcTemplate;
    ObjectMapper objectMapper;

    @Autowired
    public CouponRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        objectMapper = new ObjectMapper();
    }

    public List<Coupon> getAllCoupons() {
        String sql = "SELECT * FROM coupons;";

        return jdbcTemplate.query(sql, new CouponRowMapper());
    }

    public List<Number> addCoupons(List<Coupon> coupons) {
        String sql = "INSERT INTO coupons (type, details) VALUES (?, ?::jsonb) RETURNING coupon_id;";
        List<Number> generatedIds = new ArrayList<>();

        for (Coupon coupon : coupons) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, coupon.getType());
                try {
                    ps.setString(2, new ObjectMapper().writeValueAsString(coupon.getDetails()));  // Convert details to JSON
                } catch (JsonProcessingException _) {
                }
                return ps;
            }, keyHolder);

            // Add the generated ID to the list
            generatedIds.add(keyHolder.getKey());
        }

        return generatedIds;
    }

    public List<Map<String, Object>> cartWiseCoupons(int cartValue) {
        String sql = "select coupon_id, type, (details->'discount')::int as discount from coupons where type = 'cart-wise' and (details->>'threshold')::int <= ?::int";
        return jdbcTemplate.queryForList(sql,cartValue);
    }

    public List<Map<String, Object>> productWiseCoupons(int[] productIDs) {
        String sql = "SELECT coupon_id, type, (details->'discount')::int AS discount " +
                "FROM coupons " +
                "WHERE type = 'product-wise' " +
                "AND (details->'product_id')::int = ANY(?)";
        return jdbcTemplate.queryForList(sql ,new Object[]{productIDs});
    }
    public List<Map<String, Object>> bxGyCoupons(List<Product> cartData) {
        String sql = "SELECT coupon_id, TYPE, (APPLY_BXGY_COUPON ->> 'discount')::int as discount FROM COUPONS, APPLY_BXGY_COUPON ( coupon_id, ?::JSONB ) WHERE TYPE = 'bxgy'";
        try {
            return jdbcTemplate.queryForList(sql,new ObjectMapper().writeValueAsString(cartData));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getCouponByID(int id) {
        String sql = "SELECT * FROM coupons where coupon_id = ?";
        List<Coupon> coupons = jdbcTemplate.query(sql, new CouponRowMapper(), id);
        if (coupons.isEmpty()) {
            throw ErrorType.NotFound.getException("ID does not exist");
        } else {
            return coupons.getFirst();
        }

    }

    public Object updateCouponByID(Coupon coupon, int id) {
        String sql = "UPDATE coupons SET type = ?, details = ?::jsonb WHERE coupon_id = ?;";
        try {
            int result = jdbcTemplate.update(sql, coupon.getType(), new ObjectMapper().writeValueAsString(coupon.getDetails()),id);
            if(result==0){
                throw ErrorType.NotFound.getException("ID not found.");
            }
        } catch (JsonProcessingException _) {
        }
        return null;
    }

    public Object deleteCouponByID(int idInt) {
       String sql = "DELETE FROM coupons WHERE coupon_id = ?";
        int result = jdbcTemplate.update(sql,idInt);
        if(result==0){
            throw ErrorType.NotFound.getException("ID not found.");
        }
        return null;
    }
}
