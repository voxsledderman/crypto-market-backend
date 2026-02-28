package org.voxsledderman.cryptoExchange.domain.market;

import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PriceProvider {
    BigDecimal getCurrentPrice(String ticker);
    Map<String, CryptoInfo> getFullMarketData(List<String> tickers);
}
