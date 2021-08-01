package com.playtomic.tests.wallet.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class WalletException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String description;
}
