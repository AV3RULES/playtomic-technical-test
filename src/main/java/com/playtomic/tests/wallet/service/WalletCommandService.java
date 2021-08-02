package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class WalletCommandService {

    private final List<ThirdPartyPaymentService> thirdPartyPaymentServices;
    private final ModelMapper modelMapper;
    private final WalletRepository walletRepository;

    @Bulkhead(name = "command", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<WalletDto> charge(int id, String chargeAmount) {
        return walletRepository.findById(id)
                .map(walletEntity -> WalletEntity.builder()
                        .id(walletEntity.getId())
                        .amountCurrency(walletEntity.getAmountCurrency())
                        .amountValue(walletEntity.getAmountValue().subtract(new BigDecimal(chargeAmount)))
                        .build())
                .map(walletRepository::save)
                .map(walletEntity -> CompletableFuture.completedFuture(modelMapper.map(walletEntity, WalletDto.class)))
                .orElse(CompletableFuture.failedFuture(new WalletException(HttpStatus.NOT_FOUND, "Wallet not found by id " + id)));
    }

    @Bulkhead(name = "command", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<WalletDto> recharge(int id, String rechargeAmount, String paymentServiceType) {
        try {
            thirdPartyPaymentServices.stream()
                    .filter(paymentService -> paymentService.isSatisfiedBy(paymentServiceType))
                    .findAny()
                    .orElseThrow(() -> new WalletException(HttpStatus.BAD_REQUEST, "ThirdParty payment service type not support"))
                    .charge(new BigDecimal(rechargeAmount));
        } catch (WalletException exception) {
            log.info("Error in third party payment service: {}", exception.getDescription());
            return CompletableFuture.failedFuture(exception);
        }
        return walletRepository.findById(id)
                .map(walletEntity -> WalletEntity.builder()
                        .id(walletEntity.getId())
                        .amountCurrency(walletEntity.getAmountCurrency())
                        .amountValue(walletEntity.getAmountValue().add(new BigDecimal(rechargeAmount)))
                        .build())
                .map(walletRepository::save)
                .map(walletEntity -> CompletableFuture.completedFuture(modelMapper.map(walletEntity, WalletDto.class)))
                .orElse(CompletableFuture.failedFuture(new WalletException(HttpStatus.NOT_FOUND, "Wallet not found by id " + id)));
    }
}
