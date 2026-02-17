package org.voxsledderman.cryptoExchange.domain.repositories.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.WalletEntity;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class OrmLiteWalletRepository implements WalletRepository {

    private final Dao<WalletEntity, UUID> walletDao;

    public OrmLiteWalletRepository(ConnectionSource connectionSource) throws SQLException {
        this.walletDao = DaoManager.createDao(connectionSource, WalletEntity.class);
    }

    @Override
    public Optional<Wallet> findByUuid(UUID uuid) {
       return Optional.empty();

    }

    @Override
    public void save(Wallet wallet) {

    }
}
