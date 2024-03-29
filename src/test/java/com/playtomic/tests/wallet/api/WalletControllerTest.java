package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WalletControllerTest {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
    }

    @Test
    void shouldReturnMonoResponseEntityOK_givenId_whenFindByIdOK() {
        int givenId = 123;
        WalletEntity givenWalletEntity = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        walletRepository.save(givenWalletEntity);

        webTestClient.get()
                .uri("/wallet/{id}", givenId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnMonoResponseEntityNotFound_givenId_whenFindByIdKO() {
        int givenId = 123;

        webTestClient.get()
                .uri("/wallet/{id}", givenId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnMonoResponseEntityOK_givenIdAndAmount_whenChargeOK() {
        int givenId = 123;
        String givenChargeAmount = "10.00";
        WalletEntity givenWalletEntity = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        walletRepository.save(givenWalletEntity);

        webTestClient.put()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/wallet/charge")
                                .queryParam("id", givenId)
                                .queryParam("amount", givenChargeAmount)
                                .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnMonoResponseEntityNotFound_givenIdAndAmount_whenChargeKO() {
        int givenId = 123;
        String givenChargeAmount = "10.00";

        webTestClient.put()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/wallet/charge")
                                .queryParam("id", givenId)
                                .queryParam("amount", givenChargeAmount)
                                .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @ParameterizedTest
    @ValueSource(strings = {"paypal", "stripe"})
    void shouldReturnMonoResponseEntityOK_givenIdAndAmount_whenRechargeOK(String paymentServiceType) {
        int givenId = 123;
        String givenChargeAmount = "10.00";
        WalletEntity givenWalletEntity = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        walletRepository.save(givenWalletEntity);

        webTestClient.put()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/wallet/recharge")
                                .queryParam("id", givenId)
                                .queryParam("amount", givenChargeAmount)
                                .queryParam("paymentServiceType", paymentServiceType)
                                .build())
                .exchange()
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @MethodSource("invalidAmountByPaymentType")
    void shouldReturnMonoResponseEntityBAD_REQUEST_givenIdAndAmount_whenAmountInvalidKO(String paymentServiceType, String givenChargeAmount) {
        int givenId = 123;

        webTestClient.put()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/wallet/recharge")
                                .queryParam("id", givenId)
                                .queryParam("amount", givenChargeAmount)
                                .queryParam("paymentServiceType", paymentServiceType)
                                .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    private static Stream<Arguments> invalidAmountByPaymentType() {
        return Stream.of(
                Arguments.of("paypal", "9.00"),
                Arguments.of("stripe", "10.50")
        );
    }
}