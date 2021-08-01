package com.playtomic.tests.wallet.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletDto {
    private int id;
    private String amountCurrency;
    private BigDecimal amountValue;
}
