package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.persistance.WalletEntity;
import com.playtomic.tests.wallet.persistance.WalletRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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

    @Test
    void shouldReturnMonoResponseEntityOK_givenId_whenFindByIdOK() {
        int givenId = 123;
        WalletEntity givenWalletEntity = WalletEntity.builder().id(givenId).amountCurrency("EUR").amountValue(new BigDecimal("42.00")).build();
        walletRepository.save(givenWalletEntity);

        ResponseEntity<WalletDto> expectedResponseEntity = ResponseEntity.ok(modelMapper.map(givenWalletEntity, WalletDto.class));

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
}