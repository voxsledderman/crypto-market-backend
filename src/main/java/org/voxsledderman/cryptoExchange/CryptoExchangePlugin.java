package org.voxsledderman.cryptoExchange;

import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.infrastructure.config.ApplicationBootstrap;
import org.voxsledderman.cryptoExchange.infrastructure.config.ConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.providers.BinanceWebSocketProvider;

@Getter
public final class CryptoExchangePlugin extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private BinanceWebSocketProvider binanceWebSocketProvider;
    private ConnectionSource connectionSource;
    @Override
    public void onEnable() {
        binanceWebSocketProvider = new BinanceWebSocketProvider(configManager.getTrackedTickers());
        ApplicationBootstrap appBootstrap = new ApplicationBootstrap(configManager, getDataFolder());
        connectionSource = appBootstrap.connectToDB();
    }


    @Override
    public void onDisable() {
        try {
            connectionSource.close();
        } catch (Exception e) {
            getLogger().severe("Failed to close database connection");
            throw new RuntimeException(e);
        }
    }
}
