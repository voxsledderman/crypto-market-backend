package org.voxsledderman.cryptoExchange;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.infrastructure.config.ConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.providers.BinanceWebSocketProvider;

@Getter
public final class CryptoExchangePlugin extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private BinanceWebSocketProvider binanceWebSocketProvider;
    @Override
    public void onEnable() {
        binanceWebSocketProvider = new BinanceWebSocketProvider(configManager.getTrackedTickers());
    }

    @Override
    public void onDisable() {

    }
}
