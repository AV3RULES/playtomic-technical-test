package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.model.PaymentServiceType.PAY_PAL;
import static com.playtomic.tests.wallet.model.PaymentServiceType.STRIPE;
import static org.junit.jupiter.api.Assertions.*;

class StripePaymentServiceTest {

    StripePaymentService stripePaymentService = new StripePaymentService();

    @Test
    public void shouldReturnTrue_givenPaymentsType_whenStripe() {
        assertTrue(stripePaymentService.isSatisfiedBy(STRIPE.getName()));
    }

    @Test
    public void shouldReturnFalse_givenPaymentsType_whenNotStripe() {
        assertFalse(stripePaymentService.isSatisfiedBy(PAY_PAL.getName()));
    }

    @Test
    void shouldThrowException_givenAmount_whenDobleValue() {
        assertThrows(WalletException.class, () -> stripePaymentService.charge(new BigDecimal("5.5")));
    }

    @Test
    void shouldNotThrow_givenAmount_whenIntegerValue() {
        assertDoesNotThrow(() -> stripePaymentService.charge(new BigDecimal(5)));
    }

}