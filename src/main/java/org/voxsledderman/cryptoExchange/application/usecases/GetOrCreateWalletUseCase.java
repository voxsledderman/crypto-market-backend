package org.voxsledderman.cryptoExchange.application.usecases;

import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.repositories.WalletRepository;

import java.util.HashMap;
import java.util.UUID;

public class GetOrCreateWalletUseCase {
    private final WalletRepository walletRepository;

    public GetOrCreateWalletUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet getOrCreateWallet(UUID walletUuid){
        if(walletUuid == null){
            throw new IllegalArgumentException("Provided uuid is null");
        }
        return walletRepository.findByUuid(walletUuid).orElseGet(() -> {
            Wallet createdWallet = new Wallet(walletUuid, new HashMap<>());
            walletRepository.save(createdWallet);
            return createdWallet;
        });
    }
}
