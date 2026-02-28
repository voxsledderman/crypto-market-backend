package org.voxsledderman.cryptoExchange.infrastructure.persistence.daos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter
@DatabaseTable(tableName = "trade_orders")
public class TradeOrderDao {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "wallet_owner_uuid", canBeNull = false, index = true)
    private WalletDao wallet;

    @DatabaseField(canBeNull = false)
    private String ticker;

    @DatabaseField(canBeNull = false)
    private BigDecimal amount;

    @DatabaseField(columnName = "open_price", canBeNull = false)
    private BigDecimal openPrice;

//    @DatabaseField(columnName = "open_time", canBeNull = false)
//    private LocalDateTime openTime;

    @DatabaseField(columnName = "position_state", canBeNull = false)
    private PositionState positionState;

}
