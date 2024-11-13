package com.monkcommerce.coupon_demo.Model;

import java.util.List;

public class BuyXGetYDetails {
    List<Product> buy_products;
    List<Product> get_products;
    int repition_limit;

    public BuyXGetYDetails(List<Product> buy_products, List<Product> get_products, int repition_limit) {
        this.buy_products = buy_products;
        this.get_products = get_products;
        this.repition_limit = repition_limit;
    }

    public int getRepition_limit() {
        return repition_limit;
    }

    public void setRepition_limit(int repition_limit) {
        this.repition_limit = repition_limit;
    }

    public List<Product> getBuy_products() {
        return buy_products;
    }

    public void setBuy_products(List<Product> buy_products) {
        this.buy_products = buy_products;
    }

    public List<Product> getGet_products() {
        return get_products;
    }

    public void setGet_products(List<Product> get_products) {
        this.get_products = get_products;
    }
}
