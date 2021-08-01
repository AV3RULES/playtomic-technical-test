package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.service.WalletQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class WalletController {

    private final WalletQueryService walletQueryService;

    @GetMapping("/wallet/{id}")
    public Mono<ResponseEntity<WalletDto>> retrieveWalletById(@PathVariable int id) {
        return walletQueryService.retrieveWalletDataById(id)
                .map(wallet -> new ResponseEntity<>(wallet, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
