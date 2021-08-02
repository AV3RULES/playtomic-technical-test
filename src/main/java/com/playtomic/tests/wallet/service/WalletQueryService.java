package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class WalletQueryService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;

    @Bulkhead(name = "query", type = Bulkhead.Type.THREADPOOL)
    public Mono<WalletDto> retrieveWalletDataById(int id) {
        return walletRepository.findById(id)
                .map(walletEntity -> Mono.just(modelMapper.map(walletEntity, WalletDto.class)))
                .orElse(Mono.empty());
    }
}
