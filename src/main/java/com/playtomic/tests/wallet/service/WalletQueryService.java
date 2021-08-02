package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class WalletQueryService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;

    @Bulkhead(name = "query", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<WalletDto> retrieveWalletDataById(int id) {
        return walletRepository.findById(id)
                .map(walletEntity -> CompletableFuture.completedFuture(modelMapper.map(walletEntity, WalletDto.class)))
                .orElse(CompletableFuture.failedFuture(new WalletException(HttpStatus.NOT_FOUND, "Wallet not found by id " + id)));
    }
}
