package com.playtomic.tests.wallet.model;

import lombok.Data;

@Data
public class WalletDto {
    private int id;
    private String amountCurrency;
    private String amountValue;
}
