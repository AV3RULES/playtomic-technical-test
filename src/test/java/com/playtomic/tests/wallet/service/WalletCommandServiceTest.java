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
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletCommandServiceTest {

    @Spy
    private final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletCommandService walletCommandService;

    @Test
    void shouldReturnUpdatedWalletDto_givenIdAndAmount_whenChargeOK() {
        //given
        int givenId = 123;
        String givenChargeAmount = "10.00";
        WalletEntity walletEntityStored = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        WalletEntity walletEntityUpdated = WalletEntity.builder().id(givenId).amountCurrency("EUR")
                .amountValue(walletEntityStored.getAmountValue().subtract(new BigDecimal(givenChargeAmount))).build();
        when(walletRepository.findById(givenId)).thenReturn(Optional.of(walletEntityStored));
        when(walletRepository.save(walletEntityUpdated)).thenReturn(walletEntityUpdated);

        WalletDto expectedWalletDto = modelMapper.map(walletEntityUpdated, WalletDto.class);

        //when
        var actualWalletDto = walletCommandService.charge(givenId, givenChargeAmount);

        //then
        StepVerifier.create(actualWalletDto)
                .expectNext(expectedWalletDto)
                .expectComplete()
                .verify();
    }

    @Test
    void shouldReturnMonoEmpty_givenId_whenWalletNotExist() {
        //given
        int givenId = 123;
        String givenChargeAmount = "10.00";
        when(walletRepository.findById(givenId)).thenReturn(Optional.empty());

        //when
        var actualWalletDto = walletCommandService.charge(givenId, givenChargeAmount);

        //then
        StepVerifier.create(actualWalletDto)
                .expectComplete()
                .verify();
    }

    @Test
    void shouldReturnUpdatedWalletDto_givenIdAndAmount_whenRechargeOK() {
        //given
        int givenId = 123;
        String givenRechargeAmount = "10.00";
        WalletEntity walletEntityStored = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        WalletEntity walletEntityUpdated = WalletEntity.builder().id(givenId).amountCurrency("EUR")
                .amountValue(walletEntityStored.getAmountValue().add(new BigDecimal(givenRechargeAmount))).build();
        when(walletRepository.findById(givenId)).thenReturn(Optional.of(walletEntityStored));
        when(walletRepository.save(walletEntityUpdated)).thenReturn(walletEntityUpdated);

        WalletDto expectedWalletDto = modelMapper.map(walletEntityUpdated, WalletDto.class);

        //when
        var actualWalletDto = walletCommandService.recharge(givenId, givenRechargeAmount);

        //then
        StepVerifier.create(actualWalletDto)
                .expectNext(expectedWalletDto)
                .expectComplete()
                .verify();
    }
}