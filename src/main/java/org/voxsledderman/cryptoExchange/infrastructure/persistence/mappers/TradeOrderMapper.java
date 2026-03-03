package org.voxsledderman.cryptoExchange.infrastructure.persistence.mappers;

import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.TradeOrderDao;
import org.voxsledderman.cryptoExchange.infrastructure.persistence.daos.WalletDao;

import java.util.UUID;

public class TradeOrderMapper {
    public static TradeOrderDao toDao(TradeOrder tradeOrder, WalletDao walletDao){
        return new TradeOrderDao(tradeOrder.getId(), walletDao, tradeOrder.getTicker(), tradeOrder.getAmount(),
                tradeOrder.getOpenPrice(), tradeOrder.getOpenTime() ,tradeOrder.getPositionState());
    }
    public static TradeOrder fromDao(TradeOrderDao dao, UUID ownerUuid){
        return new TradeOrder(dao.getId(), dao.getTicker(), ownerUuid, dao.getAmount(), dao.getOpenPrice(), dao.getOpenTime(), dao.getPositionState());
    }
}
