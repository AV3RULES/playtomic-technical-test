package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.service.WalletCommandService;
import com.playtomic.tests.wallet.service.WalletQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class WalletController {

    private final WalletQueryService walletQueryService;
    private final WalletCommandService walletCommandService;

    @GetMapping("/wallet/{id}")
    public Mono<ResponseEntity<WalletDto>> retrieveWalletById(@PathVariable int id) {
        return Mono.fromFuture(walletQueryService.retrieveWalletDataById(id))
                .map(wallet -> new ResponseEntity<>(wallet, HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(((WalletException) error).getHttpStatus())));
    }

    @PutMapping("/wallet/charge")
    public Mono<ResponseEntity<WalletDto>> chargeAmount(@RequestParam int id, @RequestParam String amount) {
        return Mono.fromFuture(walletCommandService.charge(id, amount))
                .map(wallet -> new ResponseEntity<>(wallet, HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(((WalletException) error).getHttpStatus())));
    }

    @PutMapping("/wallet/recharge")
    public Mono<ResponseEntity<WalletDto>> rechargeAmount(@RequestParam int id, @RequestParam String amount, @RequestParam String paymentServiceType) {
        return Mono.fromFuture(walletCommandService.recharge(id, amount, paymentServiceType))
                .map(wallet -> new ResponseEntity<>(wallet, HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(((WalletException) error).getHttpStatus())));
    }
}
