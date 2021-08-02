package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.service.ThirdPartyPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.model.PaymentServiceType.STRIPE;

@Service
public class StripePaymentService implements ThirdPartyPaymentService {

    @Override
    public boolean isSatisfiedBy(String paymentsType) {
        return STRIPE.getName().equals(paymentsType);
    }

    @Override
    public void charge(BigDecimal amount) {
        if (amount.doubleValue() != amount.intValue()) {
            throw new WalletException(HttpStatus.BAD_REQUEST, "Stripe service not allow decimal charges");
        }
    }
}
