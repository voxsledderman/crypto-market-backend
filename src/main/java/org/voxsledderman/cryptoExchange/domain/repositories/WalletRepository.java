package org.voxsledderman.cryptoExchange.domain.repositories;

import org.voxsledderman.cryptoExchange.domain.entities.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Optional<Wallet> findByUuid(UUID uuid);
    void save(Wallet wallet);
    void invalidate(UUID uuid);


}
