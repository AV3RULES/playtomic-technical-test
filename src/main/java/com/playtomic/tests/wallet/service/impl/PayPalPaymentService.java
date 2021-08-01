package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.service.ThirdPartyPaymentService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * A real implementation would call to a third party's payment service (such as Stripe, Paypal, Redsys...).
 *
 * This is a dummy implementation which throws an error when trying to change less than 10€.
 */
@Service
public class PayPalPaymentService implements ThirdPartyPaymentService {
    private final BigDecimal threshold = new BigDecimal(10);

    @Override
    public void charge(BigDecimal amount) throws WalletException {
        if (amount.compareTo(threshold) < 0) {
            throw new WalletException();
        }
    }
}
