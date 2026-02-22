package org.voxsledderman.cryptoExchange.infrastructure.persistence.mappers;

import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.WalletDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WalletMapper {

    public static WalletDao toDao(Wallet wallet){
        WalletDao walletDao = new WalletDao();
        walletDao.setOwnerUuid(wallet.getOwnerUuid());

        return walletDao;

    }
    public static Wallet fromDao(WalletDao dao){
        Map<String, List<TradeOrder>> tradeOrders = new HashMap<>();
        dao.getOrders().forEach(order ->
                tradeOrders.computeIfAbsent(order.getTicker(), k -> new ArrayList<>())
                .add(TradeOrderMapper.fromDao(order, dao.getOwnerUuid())));
        return new Wallet(dao.getOwnerUuid(), tradeOrders);
    }
}
