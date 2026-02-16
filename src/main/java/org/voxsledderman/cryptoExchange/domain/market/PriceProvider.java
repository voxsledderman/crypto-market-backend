package org.voxsledderman.cryptoExchange.domain.market;

import java.math.BigDecimal;

public interface PriceProvider {
    BigDecimal getCurrentPrice(String ticker);
}
