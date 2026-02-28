package org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.WalletDao;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.mappers.WalletMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrmLiteWalletRepository implements WalletRepository {

    private final Dao<WalletDao, UUID> walletDao;

    public OrmLiteWalletRepository(ConnectionSource connectionSource) throws SQLException {
        this.walletDao = DaoManager.createDao(connectionSource, WalletDao.class);

    }

    @Override
    public Optional<Wallet> findByUuid(UUID uuid) {
        try {
            return Optional.ofNullable(walletDao.queryForId(uuid))
                    .map(WalletMapper::fromDao);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Wallet wallet) {
        try {
            walletDao.createOrUpdate(WalletMapper.toDao(wallet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void invalidate(UUID uuid) {

    }
}
