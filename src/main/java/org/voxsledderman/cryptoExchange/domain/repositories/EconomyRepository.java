package org.voxsledderman.cryptoExchange.domain.repositories;

import java.math.BigDecimal;
import java.util.UUID;

public interface EconomyRepository {

    BigDecimal getBalance(UUID uuid);
    boolean deposit(UUID uuid, BigDecimal amount);
    boolean withdraw(UUID uuid, BigDecimal amount);
    boolean hasEnough(UUID uuid, BigDecimal amount);
}
