package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletQueryService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public Mono<WalletDto> retrieveWalletDataById(int id) {
        return Mono.just(walletRepository.findById(id)
                .map(walletEntity -> modelMapper.map(walletEntity, WalletDto.class)).get());
    }
}
