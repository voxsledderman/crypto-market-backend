package org.voxsledderman.cryptoExchange.domain.repositories;

import org.voxsledderman.cryptoExchange.domain.entities.Wallet;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Optional<Wallet> findByUuid(UUID uuid) throws SQLException;
    void save(Wallet wallet) throws SQLException;


}
