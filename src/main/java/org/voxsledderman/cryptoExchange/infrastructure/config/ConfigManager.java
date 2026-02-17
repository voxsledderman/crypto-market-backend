package org.voxsledderman.cryptoExchange.infrastructure.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.voxsledderman.cryptoExchange.CryptoExchangePlugin;
import org.voxsledderman.cryptoExchange.domain.market.QuoteCurrency;
import org.voxsledderman.cryptoExchange.domain.validators.MarketConfigValidator;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ConfigManager {

    private final CryptoExchangePlugin plugin;
    private QuoteCurrency quoteCurrency;
    private List<String> trackedTickers;
    private BigDecimal spread;
    private boolean mySqlEnabled;
    private MySqlConnectionData mySqlConnectionData;

    public ConfigManager(CryptoExchangePlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        String currency = config.getString("market.quote-currency", "USDT").toUpperCase();
        List<String> tickers = config.getStringList("market.tracked-cryptos");
        BigDecimal spread = BigDecimal.valueOf(config.getDouble("market.spread",0.5));

        this.quoteCurrency = MarketConfigValidator.validateCurrency(currency);
        this.trackedTickers = List.copyOf(MarketConfigValidator.validateTrackedTickers(tickers));
        this.spread = MarketConfigValidator.validateSpread(spread);

        Boolean mySql = config.getBoolean("MySQL_database.enabled", false);



    }
}