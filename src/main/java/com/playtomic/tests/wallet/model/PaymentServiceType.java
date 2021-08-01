package com.playtomic.tests.wallet.model;

public enum PaymentServiceType {
    PAY_PAL("paypal"),
    STRIPE("stripe");

    private final String name;

    PaymentServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
