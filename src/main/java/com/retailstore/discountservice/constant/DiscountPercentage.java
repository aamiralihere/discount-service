package com.retailstore.discountservice.constant;public enum DiscountPercentage {    CUSTOMER_PERCENTAGE(0.05),    AFFILIATE_PERCENTAGE(0.1),    EMPLOYEE_PERCENTAGE(0.3);    private final Double discount;    DiscountPercentage(Double discount) {        this.discount = discount;    }    public Double getDiscount() {        return discount;    }}