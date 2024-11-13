CREATE TABLE coupons (
    coupon_id SERIAL PRIMARY KEY,
    type text,
    details jsonb
);
