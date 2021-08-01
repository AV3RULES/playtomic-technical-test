package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.service.ThirdPartyPaymentService;

import java.math.BigDecimal;

public class StripePaymentService implements ThirdPartyPaymentService {

    @Override
    public boolean isSatisfiedBy(String paymentsType) {
        return false;
    }

    @Override
    public void charge(BigDecimal amount) {
    }
}
