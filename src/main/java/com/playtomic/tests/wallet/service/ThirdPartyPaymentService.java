package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;

import java.math.BigDecimal;

public interface ThirdPartyPaymentService {
    void charge(BigDecimal amount) throws WalletException;
}
