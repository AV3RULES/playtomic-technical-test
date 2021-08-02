package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletException;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import com.playtomic.tests.wallet.service.impl.PayPalPaymentService;
import com.playtomic.tests.wallet.service.impl.StripePaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletCommandServiceTest {

    @Spy
    private ModelMapper modelMapper;
    @Spy
    private List<ThirdPartyPaymentService> thirdPartyPaymentServices;

    @Mock
    private PayPalPaymentService payPalPaymentService;
    @Mock
    private StripePaymentService stripePaymentService;
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
    void shouldReturnUpdatedWalletDto_givenIdAndAmount_whenPaymentServiceTypePayPal_andRechargeOK() {
        //given
        ReflectionTestUtils.setField(walletCommandService, "thirdPartyPaymentServices", initializePaymentServices());
        int givenId = 123;
        String givenRechargeAmount = "10.00";
        String givenPaymentServiceType = "paypal";
        BigDecimal amount = new BigDecimal(givenRechargeAmount);
        WalletEntity walletEntityStored = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        WalletEntity walletEntityUpdated = WalletEntity.builder().id(givenId).amountCurrency("EUR")
                .amountValue(walletEntityStored.getAmountValue().add(amount)).build();
        when(walletRepository.findById(givenId)).thenReturn(Optional.of(walletEntityStored));
        when(walletRepository.save(walletEntityUpdated)).thenReturn(walletEntityUpdated);
        when(payPalPaymentService.isSatisfiedBy(givenPaymentServiceType)).thenReturn(true);

        WalletDto expectedWalletDto = modelMapper.map(walletEntityUpdated, WalletDto.class);

        //when
        var actualWalletDto = walletCommandService.recharge(givenId, givenRechargeAmount, givenPaymentServiceType);

        //then
        StepVerifier.create(actualWalletDto)
                .expectNext(expectedWalletDto)
                .expectComplete()
                .verify();
        verify(payPalPaymentService).charge(amount);
        verify(payPalPaymentService).isSatisfiedBy(givenPaymentServiceType);
    }

    @Test
    void shouldReturnUpdatedWalletDto_givenIdAndAmount_whenPaymentServiceTypeStripe_andRechargeOK() {
        //given
        ReflectionTestUtils.setField(walletCommandService, "thirdPartyPaymentServices", initializePaymentServices());
        int givenId = 123;
        String givenRechargeAmount = "10.00";
        String givenPaymentServiceType = "stripe";
        BigDecimal amount = new BigDecimal(givenRechargeAmount);
        WalletEntity walletEntityStored = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        WalletEntity walletEntityUpdated = WalletEntity.builder().id(givenId).amountCurrency("EUR")
                .amountValue(walletEntityStored.getAmountValue().add(amount)).build();
        when(walletRepository.findById(givenId)).thenReturn(Optional.of(walletEntityStored));
        when(walletRepository.save(walletEntityUpdated)).thenReturn(walletEntityUpdated);
        when(payPalPaymentService.isSatisfiedBy(givenPaymentServiceType)).thenReturn(false);
        when(stripePaymentService.isSatisfiedBy(givenPaymentServiceType)).thenReturn(true);

        WalletDto expectedWalletDto = modelMapper.map(walletEntityUpdated, WalletDto.class);

        //when
        var actualWalletDto = walletCommandService.recharge(givenId, givenRechargeAmount, givenPaymentServiceType);

        //then
        StepVerifier.create(actualWalletDto)
                .expectNext(expectedWalletDto)
                .expectComplete()
                .verify();
        verify(stripePaymentService).charge(amount);
        verify(stripePaymentService).isSatisfiedBy(givenPaymentServiceType);
    }

    @Test
    void shouldReturnMonoError_givenIdAndAmount_whenPaymentServiceTypeNotSupport() {
        //given
        ReflectionTestUtils.setField(walletCommandService, "thirdPartyPaymentServices", initializePaymentServices());
        int givenId = 123;
        String givenRechargeAmount = "10.00";
        String givenPaymentServiceType = "invalid";
        BigDecimal amount = new BigDecimal(givenRechargeAmount);
        WalletException exception = new WalletException(HttpStatus.BAD_REQUEST, "ThirdParty payment service not support");
        when(payPalPaymentService.isSatisfiedBy(givenPaymentServiceType)).thenReturn(false);
        when(stripePaymentService.isSatisfiedBy(givenPaymentServiceType)).thenReturn(false);

        //when
        var actualWalletDto = walletCommandService.recharge(givenId, givenRechargeAmount, givenPaymentServiceType);

        //then
        StepVerifier.create(actualWalletDto)
                .expectErrorMatches(e -> e instanceof WalletException && exception.equals(e))
                .verify();
        verify(payPalPaymentService).isSatisfiedBy(givenPaymentServiceType);
        verify(stripePaymentService).isSatisfiedBy(givenPaymentServiceType);
    }


    @Test
    void shouldReturnMonoError_givenIdAndAmount_whenThirdPartyPaymentServiceReturnKO() {
        //given
        ReflectionTestUtils.setField(walletCommandService, "thirdPartyPaymentServices", initializePaymentServices());
        int givenId = 123;
        String givenRechargeAmount = "10.00";
        String givenPaymentServiceType = "paypal";
        BigDecimal amount = new BigDecimal(givenRechargeAmount);
        WalletException exception = new WalletException(HttpStatus.BAD_REQUEST, "Paypal service not allow charges less than 10");
        when(payPalPaymentService.isSatisfiedBy(givenPaymentServiceType)).thenReturn(true);
        doThrow(exception).when(payPalPaymentService).charge(amount);

        //when
        var actualWalletDto = walletCommandService.recharge(givenId, givenRechargeAmount, givenPaymentServiceType);

        //then
        StepVerifier.create(actualWalletDto)
                .expectErrorMatches(e -> e instanceof WalletException && exception.equals(e))
                .verify();
        verify(payPalPaymentService).charge(amount);
    }

    private List<ThirdPartyPaymentService> initializePaymentServices() {
        return List.of(payPalPaymentService, stripePaymentService);
    }
}