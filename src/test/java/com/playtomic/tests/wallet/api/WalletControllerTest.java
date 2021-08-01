package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WalletControllerTest {

    private final ModelMapper modelMapper = new ModelMapper();

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
    void shouldReturnMonoResponseEntityOK_givenId_whenFindByIdKO() {
        int givenId = 123;

        webTestClient.get()
                .uri("/wallet/{id}", givenId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnMonoResponseEntityOK_givenId_whenChargeOK() {
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
}