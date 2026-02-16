package org.voxsledderman.cryptoExchange.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TradeOrder {
    private final UUID id;
    private String ticker;
    private BigDecimal amount;
    private BigDecimal openPrice;
    private LocalDateTime timestamp;

    private BigDecimal getTradeValueOnOpen(){
        return amount.multiply(openPrice);
    }
    private BigDecimal getTradeValueNow(BigDecimal currentPrice){
        return amount.multiply(currentPrice);
    }
    public BigDecimal getProfit(BigDecimal currentPrice){
        return getTradeValueNow(currentPrice).subtract(getTradeValueOnOpen());
    }
}
