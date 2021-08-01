package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonDeserialize(as = WalletDto.class)
public class WalletDto {
    private int id;
    private String amountCurrency;
    private String amountValue;
}
