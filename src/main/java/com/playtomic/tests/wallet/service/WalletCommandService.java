package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletCommandService {

    private final WalletRepository walletRepository;

    public Mono<WalletDto> charge(int id, String chargeAmount) {
        return Mono.empty();
    }
}
