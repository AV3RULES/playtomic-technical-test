package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.WalletDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletQueryService {

    public Mono<WalletDto> retrieveWalletDataById(int id) {
        return Mono.empty();
    }
}
