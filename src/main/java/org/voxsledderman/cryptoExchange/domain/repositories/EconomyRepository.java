package org.voxsledderman.cryptoExchange.domain.repositories;

import java.util.UUID;

public interface EconomyRepository {

    double getBalance(UUID uuid);
    boolean deposit(UUID uuid, double amount);
    boolean withdraw(UUID uuid, double amount);
    boolean hasEnough(UUID uuid, double amount);
}
