package com.playtomic.tests.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
    private int id;
    private String amountCurrency;
    private BigDecimal amountValue;
}
