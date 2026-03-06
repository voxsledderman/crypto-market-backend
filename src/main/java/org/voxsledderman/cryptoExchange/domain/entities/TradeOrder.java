package org.voxsledderman.cryptoExchange.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeOrder {
    private Long id;
    private String ticker;
    private UUID walletUuid;
    private BigDecimal amount;
    private BigDecimal openPrice;
    private LocalDateTime openTime;
    private PositionState positionState;
    private BigDecimal closedValue;

    public BigDecimal getTradeValueOnOpen(){
        return amount.multiply(openPrice);
    }
    public BigDecimal getTradeValueNow(BigDecimal currentPrice){
        return amount.multiply(currentPrice);
    }
    public BigDecimal getProfitOpened(BigDecimal currentPrice){
        if (!isOpened()) throw new IllegalStateException("Position is closed");
        return getTradeValueNow(currentPrice).subtract(getTradeValueOnOpen());
    }
    public BigDecimal getProfitClosed() {
        if (isOpened()) throw new IllegalStateException("Position is still opened");
        return closedValue.subtract(getTradeValueOnOpen());
    }
    public boolean isOpened(){
        return positionState == PositionState.OPENED;
    }
}
