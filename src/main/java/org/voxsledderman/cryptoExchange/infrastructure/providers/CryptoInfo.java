package org.voxsledderman.cryptoExchange.infrastructure.providers;

import java.math.BigDecimal;

public record CryptoInfo(String fullName, BigDecimal price, String changePercent) {}

