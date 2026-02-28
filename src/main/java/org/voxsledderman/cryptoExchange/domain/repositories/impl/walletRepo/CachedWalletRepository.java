package org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo;

import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CachedWalletRepository implements WalletRepository {
    private final Map<UUID, Wallet> cachedWallets;
    private final WalletRepository databaseRepo;

    public CachedWalletRepository(WalletRepository databaseRepo) {
        cachedWallets = new ConcurrentHashMap<>();
        this.databaseRepo = databaseRepo;
    }

    @Override
    public Optional<Wallet> findByUuid(UUID uuid) {
        Wallet wallet = cachedWallets.get(uuid);
        if (wallet != null) return Optional.of(wallet);

        return databaseRepo.findByUuid(uuid)
                .map(dbWallet -> {
                    cachedWallets.put(dbWallet.getOwnerUuid(), dbWallet);
                    return dbWallet;
                });
    }

    @Override
    public void save(Wallet wallet) {
        databaseRepo.save(wallet);
        cachedWallets.put(wallet.getOwnerUuid(), wallet);
    }

    @Override
    public void invalidate(UUID uuid){
        cachedWallets.remove(uuid);
    }
}
