package com.monkcommerce.coupon_demo.Repository;

import com.monkcommerce.coupon_demo.Model.Coupon;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponRowMapper implements RowMapper<Coupon> {
    @Override
    public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setCoupon_id(rs.getInt("coupon_id"));
        coupon.setType(rs.getString("type"));
        JSONObject jsonObject = new JSONObject(rs.getObject("details").toString());
        coupon.setDetails(jsonObject.toMap());
        return coupon;
    }
}
