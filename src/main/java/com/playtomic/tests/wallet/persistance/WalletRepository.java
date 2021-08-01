package com.playtomic.tests.wallet.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, Integer> {
}
