package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.exception.WalletException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.model.PaymentServiceType.PAY_PAL;
import static com.playtomic.tests.wallet.model.PaymentServiceType.STRIPE;
import static org.junit.jupiter.api.Assertions.*;

public class PayPalPaymentServiceTest {

    PayPalPaymentService payPalPaymentService = new PayPalPaymentService();

    @Test
    public void shouldReturnTrue_givenPaymentsType_whenPaypal() {
        assertTrue(payPalPaymentService.isSatisfiedBy(PAY_PAL.getName()));
    }

    @Test
    public void shouldReturnFalse_givenPaymentsType_whenNotPaypal() {
        assertFalse(payPalPaymentService.isSatisfiedBy(STRIPE.getName()));
    }

    @Test
    public void shouldThrowException_givenAmount_whenValueLowerThan10() throws WalletException {
        WalletException expectedException = new WalletException(HttpStatus.BAD_REQUEST, "Paypal service not allow charges less than 10");
        WalletException actualException = assertThrows(WalletException.class, () -> payPalPaymentService.charge(new BigDecimal(5)));
        assertEquals(expectedException, actualException);
    }

    @Test
    public void shouldNotThrowException_givenAmount_whenValueGreaterOrEqualsThan10() throws WalletException {
        assertDoesNotThrow(() -> payPalPaymentService.charge(new BigDecimal(15)));
    }
}
