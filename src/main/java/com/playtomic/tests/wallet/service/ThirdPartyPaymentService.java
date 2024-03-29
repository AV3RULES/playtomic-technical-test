package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;

import java.math.BigDecimal;

public interface ThirdPartyPaymentService { // TODO implement web client and circuit breaker
    boolean isSatisfiedBy(String paymentType);
    void charge(BigDecimal amount) throws WalletException;
}
