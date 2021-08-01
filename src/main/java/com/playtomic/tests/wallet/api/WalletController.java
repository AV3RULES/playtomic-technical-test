package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.service.WalletQueryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class WalletController {

    private final WalletQueryService walletQueryService;

    @GetMapping("/wallet/{id}")
    public Mono<ResponseEntity<WalletDto>> retrieveWalletById(@PathVariable int id) throws WalletException {
        return Mono.empty();
    }
}
