package org.voxsledderman.cryptoExchange.domain.entities;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CryptoCurrency {
    private final String ticker;
    private String name;
    private BigDecimal currentPrice;
    private String OneDayChangePercentage;

}
