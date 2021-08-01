package com.playtomic.tests.wallet.persistance;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

public class BigDecimalStringConverter implements AttributeConverter<BigDecimal, String> {

    @Override
    public String convertToDatabaseColumn(BigDecimal bigDecimal) {
        return String.valueOf(bigDecimal);
    }

    @Override
    public BigDecimal convertToEntityAttribute(String s) {
        return new BigDecimal(s);
    }
}
