package org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.TradeOrderDao;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.WalletDao;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.mappers.TradeOrderMapper;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.mappers.WalletMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrmLiteWalletRepository implements WalletRepository {

    private final Dao<WalletDao, UUID> walletDao;
    private final Dao<TradeOrderDao, Long> tradeOrderDao;

    public OrmLiteWalletRepository(ConnectionSource connectionSource) throws SQLException {
        this.walletDao = DaoManager.createDao(connectionSource, WalletDao.class);
        this.tradeOrderDao = DaoManager.createDao(connectionSource, TradeOrderDao.class);
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
       WalletDao walletEntity = WalletMapper.toDao(wallet);

        try {
            walletDao.createOrUpdate(walletEntity);
            saveOrders(wallet, walletEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveOrders(Wallet wallet, WalletDao walletEntity){
        wallet.getOrders()
                .values()
                .stream()
                .flatMap(List::stream)
                .forEach(order -> {
                    try {
                        TradeOrderDao orderEntity = TradeOrderMapper.toDao(order, walletEntity);
                        tradeOrderDao.createOrUpdate(orderEntity);
                        order.setId(orderEntity.getId());

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void invalidate(UUID uuid) {

    }
}
