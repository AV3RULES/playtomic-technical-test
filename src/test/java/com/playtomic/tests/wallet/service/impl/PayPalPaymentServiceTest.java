package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.exception.WalletException;
import org.junit.Test;

import java.math.BigDecimal;

public class PayPalPaymentServiceTest {

    PayPalPaymentService payPalPaymentService = new PayPalPaymentService();

    @Test(expected = WalletException.class)
    public void test_exception() throws WalletException {
        payPalPaymentService.charge(new BigDecimal(5));
    }

    @Test
    public void test_ok() throws WalletException {
        payPalPaymentService.charge(new BigDecimal(15));
    }
}
