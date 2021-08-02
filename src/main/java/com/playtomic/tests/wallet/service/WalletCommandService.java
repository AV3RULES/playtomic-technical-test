package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import com.playtomic.tests.wallet.service.impl.PayPalPaymentService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WalletCommandService {

    private final List<ThirdPartyPaymentService> thirdPartyPaymentServices;
    private final ModelMapper modelMapper;
    private final WalletRepository walletRepository;

    @Bulkhead(name = "command", type = Bulkhead.Type.THREADPOOL)
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

    @Bulkhead(name = "command", type = Bulkhead.Type.THREADPOOL)
    public Mono<WalletDto> recharge(int id, String rechargeAmount, String paymentServiceType) {
        try {
            thirdPartyPaymentServices.stream()
                    .filter(paymentService -> paymentService.isSatisfiedBy(paymentServiceType))
                    .findAny()
                    .orElseThrow(() -> new WalletException(HttpStatus.BAD_REQUEST, "ThirdParty payment service type not support"))
                    .charge(new BigDecimal(rechargeAmount));
        } catch (WalletException exception) {
            log.info("Error in third party payment service: {}", exception.getDescription());
            return Mono.error(exception);
        }
        return walletRepository.findById(id)
                .map(walletEntity -> WalletEntity.builder()
                        .id(walletEntity.getId())
                        .amountCurrency(walletEntity.getAmountCurrency())
                        .amountValue(walletEntity.getAmountValue().add(new BigDecimal(rechargeAmount)))
                        .build())
                .map(walletRepository::save)
                .map(walletEntity -> Mono.just(modelMapper.map(walletEntity, WalletDto.class)))
                .orElse(Mono.empty());

    }
}
