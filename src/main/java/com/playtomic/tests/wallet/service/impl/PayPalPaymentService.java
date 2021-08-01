package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.service.ThirdPartyPaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.model.PaymentServiceType.PAY_PAL;


@Service
public class PayPalPaymentService implements ThirdPartyPaymentService {
    private final BigDecimal threshold = new BigDecimal(10);

    @Override
    public boolean isSatisfiedBy(String paymentType) {
        return PAY_PAL.getName().equals(paymentType);
    }

    @Override
    public void charge(BigDecimal amount) throws WalletException {
        if (amount.compareTo(threshold) < 0) {
            throw new WalletException(HttpStatus.BAD_REQUEST, "Paypal service not allow charges less than 10");
        }
    }
}
