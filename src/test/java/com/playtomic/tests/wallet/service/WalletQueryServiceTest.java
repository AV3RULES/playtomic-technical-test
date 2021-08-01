package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletQueryServiceTest {

    @Spy
    private final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletQueryService walletQueryService;

    @Test
    void shouldReturnWalletData_givenId_whenWalletExist() {
        //given
        int givenId = 123;
        WalletEntity givenWalletEntity = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        when(walletRepository.findById(givenId)).thenReturn(Optional.of(givenWalletEntity));

        WalletDto expectedWalletDto = modelMapper.map(givenWalletEntity, WalletDto.class);

        //when
        Mono<WalletDto> actualWalletDto = walletQueryService.retrieveWalletDataById(givenId);

        StepVerifier.create(actualWalletDto)
                .expectNext(expectedWalletDto)
                .expectComplete()
                .verify();
    }

}