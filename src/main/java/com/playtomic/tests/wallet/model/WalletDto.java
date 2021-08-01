package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonDeserialize(as = WalletDto.class)
public class WalletDto {
    private int id;
    private String amountCurrency;
    private BigDecimal amountValue;
}
