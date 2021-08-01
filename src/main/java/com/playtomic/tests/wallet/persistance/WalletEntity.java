package com.playtomic.tests.wallet.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {

    @Id
    @NotNull
    @Column
    private int id;

    @Column(length = 3)
    private String amountCurrency;

    @Column
    @Convert(converter = BigDecimalStringConverter.class)
    private BigDecimal amountValue;
}
