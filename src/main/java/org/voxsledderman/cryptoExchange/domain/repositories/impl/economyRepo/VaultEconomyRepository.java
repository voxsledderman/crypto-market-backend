package org.voxsledderman.cryptoExchange.domain.repositories.impl.economyRepo;

import lombok.extern.slf4j.Slf4j;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.voxsledderman.cryptoExchange.domain.repositories.EconomyRepository;

import java.util.UUID;

@Slf4j
public class VaultEconomyRepository implements EconomyRepository {

    private final Economy economy;

    public VaultEconomyRepository(Economy economy) {
        this.economy = economy;
    }

    @Override
    public double getBalance(UUID uuid) {
        return economy.getBalance(getOfflinePlayer(uuid));
    }

    @Override
    public boolean deposit(UUID uuid, double amount) {
        return economy.depositPlayer(getOfflinePlayer(uuid), amount).transactionSuccess();
    }

    @Override
    public boolean withdraw(UUID uuid, double amount) {
       return economy.withdrawPlayer(getOfflinePlayer(uuid), amount).transactionSuccess();
    }

    @Override
    public boolean hasEnough(UUID uuid, double amount) {
        return economy.has(getOfflinePlayer(uuid), amount);
    }

    private OfflinePlayer getOfflinePlayer(UUID uuid){
        return Bukkit.getOfflinePlayer(uuid);
    }
}
