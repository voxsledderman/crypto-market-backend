package org.voxsledderman.cryptoExchange.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;

import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
public class Wallet {
    private final UUID ownerUuid;
    private final Map<String, List<TradeOrder>> activePositions = new HashMap<>();

    public void addTrade(TradeOrder order){
        activePositions.computeIfAbsent(order.getTicker(), k -> new ArrayList<>()).add(order);
    }

    public BigDecimal getTotalEarnings(Map<String, BigDecimal> currentPrices) {
        return activePositions.values().stream()
                .flatMap(List::stream)
                .map(trade -> {
                    BigDecimal currentPrice = currentPrices.get(trade.getTicker());
                    return trade.getProfit(currentPrice);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
