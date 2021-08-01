package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import com.playtomic.tests.wallet.service.impl.PayPalPaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletCommandService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final WalletRepository walletRepository;
    private final PayPalPaymentService payPalPaymentService;

    public Mono<WalletDto> charge(int id, String chargeAmount) {
        return walletRepository.findById(id)
                .map(walletEntity -> WalletEntity.builder()
                        .id(walletEntity.getId())
                        .amountCurrency(walletEntity.getAmountCurrency())
                        .amountValue(walletEntity.getAmountValue().subtract(new BigDecimal(chargeAmount)))
                        .build())
                .map(walletRepository::save)
                .map(walletEntity -> Mono.just(modelMapper.map(walletEntity, WalletDto.class)))
                .orElse(Mono.empty());
    }

    public Mono<WalletDto> recharge(int id, String rechargeAmount) {
        try {
            payPalPaymentService.charge(new BigDecimal(rechargeAmount));
            return walletRepository.findById(id)
                    .map(walletEntity -> WalletEntity.builder()
                            .id(walletEntity.getId())
                            .amountCurrency(walletEntity.getAmountCurrency())
                            .amountValue(walletEntity.getAmountValue().add(new BigDecimal(rechargeAmount)))
                            .build())
                    .map(walletRepository::save)
                    .map(walletEntity -> Mono.just(modelMapper.map(walletEntity, WalletDto.class)))
                    .orElse(Mono.empty());
        } catch (WalletException exception) {
            return Mono.error(exception);
        }
    }
}
