package org.voxsledderman.cryptoExchange.application.dtos;

import java.math.BigDecimal;

public record CryptoAssetDto(
    String ticker,
    String name,
    BigDecimal currentPricePerUnit,
    BigDecimal amount
){
    public BigDecimal assetValue(){
        return currentPricePerUnit.multiply(amount);
    }
}
