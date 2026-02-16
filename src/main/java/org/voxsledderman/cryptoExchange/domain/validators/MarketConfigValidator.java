package org.voxsledderman.cryptoExchange.domain.validators;

import org.voxsledderman.cryptoExchange.domain.market.QuoteCurrency;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MarketConfigValidator {

    public static QuoteCurrency validateCurrency(String currency){
        return Arrays.stream(QuoteCurrency.values())
                .filter(e -> e.getCurrencyTicker().equalsIgnoreCase(currency))
                .findFirst()
                .orElseGet(() ->{
                    System.err.println("Unsupported currency [" + currency + "] in config.yml, using USDT");
                    return QuoteCurrency.USD;
                });


    }
    public static List<String> validateTrackedTickers(List<String> tickers){
        return tickers.stream()
                .filter(t -> t != null && !t.isBlank())
                .toList();
    }

    public static BigDecimal validateSpread(BigDecimal spread){
        if(!(spread.compareTo(BigDecimal.ZERO) > 0 && spread.compareTo(BigDecimal.valueOf(99.0)) < 0)){
            System.err.println("Unsupported spread value [" + spread + "] in config.yml, using default spread (0.05%)");
            return BigDecimal.valueOf(0.05);
        }
        return spread;
    }
}
