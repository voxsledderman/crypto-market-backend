package org.voxsledderman.cryptoExchange.domain.repositories;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface EconomyRepository {
    Optional<BigDecimal> getBalance(UUID uuid);
    void deposit(UUID uuid, BigDecimal amount);
    void withdraw(UUID uuid, BigDecimal amount);
    boolean hasEnough(UUID uuid, BigDecimal amount);
}
