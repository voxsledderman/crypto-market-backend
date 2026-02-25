package org.voxsledderman.cryptoExchange;

import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.impl.economyRepo.VaultEconomyRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo.CachedWalletRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;
import org.voxsledderman.cryptoExchange.domain.repositories.impl.walletRepo.OrmLiteWalletRepository;
import org.voxsledderman.cryptoExchange.infrastructure.config.ApplicationBootstrap;
import org.voxsledderman.cryptoExchange.infrastructure.config.ConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.providers.BinanceWebSocketProvider;
import org.voxsledderman.cryptoExchange.service.WalletService;

import java.sql.SQLException;

@Getter
public final class CryptoExchangePlugin extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private BinanceWebSocketProvider binanceWebSocketProvider;
    private ConnectionSource connectionSource;
    private WalletRepository walletRepository;
    private EconomyRepository economyRepository;


    private WalletService walletService;

    @Override
    public void onEnable() {
        binanceWebSocketProvider = new BinanceWebSocketProvider(configManager.getTrackedTickers());
        ApplicationBootstrap appBootstrap = new ApplicationBootstrap(configManager, getDataFolder());
        connectionSource = appBootstrap.connectToDB();

        try {
            WalletRepository ormRepo = new OrmLiteWalletRepository(connectionSource);
            this.walletRepository = new CachedWalletRepository(ormRepo);

        } catch (SQLException e) {
            getLogger().severe("Error while enabling repository: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Economy economy = setupEconomy();
        if(economy == null) {
            getLogger().severe("Unable to load Vault Economy, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        economyRepository = new VaultEconomyRepository(economy);

        walletService = new WalletService(walletRepository);
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

    private Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }
}
