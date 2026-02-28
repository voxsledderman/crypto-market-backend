package org.voxsledderman.cryptoExchange.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;

import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
@Builder
public class Wallet {
    private final UUID ownerUuid;
    private final Map<String, List<TradeOrder>> orders;


    public void addTrade(TradeOrder tradeOrder){
        orders.computeIfAbsent(tradeOrder.getTicker(), k -> new ArrayList<>()).add(tradeOrder);
    }
    public void removeTrade(TradeOrder tradeOrder) throws IllegalArgumentException {
        String ticker = tradeOrder.getTicker();
        if(!orders.containsKey(ticker) || !orders.get(ticker).contains(tradeOrder)){
            throw new IllegalArgumentException("Wallet doesn't contain this TradeOrder");
        }
        orders.get(ticker).remove(tradeOrder);
    }

    public BigDecimal getTotalEarnings(Map<String, BigDecimal> currentPrices) {
        return orders.values().stream()
                .flatMap(List::stream)
                .map(trade -> {
                    BigDecimal currentPrice = currentPrices.get(trade.getTicker());
                    if (currentPrice == null) return BigDecimal.ZERO;
                    return trade.getProfit(currentPrice);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public List<TradeOrder> filterOrdersByPositionState(PositionState positionState){
        return orders.values().stream()
                .flatMap(List::stream)
                .filter(order -> order.getPositionState().equals(positionState))
                .toList();
    }
}
