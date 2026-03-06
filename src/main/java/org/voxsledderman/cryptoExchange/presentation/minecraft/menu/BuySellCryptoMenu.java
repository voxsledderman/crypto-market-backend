package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.application.usecases.BuyCryptoUseCase;
import org.voxsledderman.cryptoExchange.application.usecases.SellCryptoUseCase;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.*;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;

import java.math.BigDecimal;

public class BuySellCryptoMenu extends Menu{
    private final CryptoItem cryptoItem;
    private final BigDecimal pickedAmount;
    private final PriceProvider priceProvider;
    private final Wallet wallet;
    private final String ticker;
    private final Menu turnBackMenu;

    public BuySellCryptoMenu(MenuType menuType, CryptoItem cryptoItem, BigDecimal pickedAmount, PriceProvider priceProvider, Wallet wallet, Menu turnBackMenu) {
        super(cryptoItem.getPlugin(), cryptoItem.getMenuContext(), menuType);
        this.cryptoItem = cryptoItem;
        this.pickedAmount = pickedAmount;
        this.priceProvider = priceProvider;
        this.wallet = wallet;
        this.ticker = cryptoItem.getTicker();
        this.turnBackMenu = turnBackMenu;
    }

    @Override
    public Gui setupGui() {
        return Gui.normal()
                .setStructure(
                        "b b b b . s s s E",
                        "b b b b C s s s s",
                        "B b b b H s s s s"
                )
                .addIngredient('C', cryptoItem)
                .addIngredient('H', new PickAmountItem(pickedAmount, getPlugin(), getMenuContext(), cryptoItem, turnBackMenu))
                .addIngredient('B', new TurnBackItem(turnBackMenu))
                .addIngredient('E', new CloseItem())
                .addIngredient('b', new BuyItem(new BuyCryptoUseCase(
                        getWalletRepository(), getEconomyRepository(), getAppConfigManager()
                ), pickedAmount, getPlugin(), wallet, ticker, priceProvider.getCurrentData(ticker)))
                .addIngredient('s', new SellItem(new SellCryptoUseCase(
                        getEconomyRepository(), getWalletRepository(), priceProvider),
                        pickedAmount, getPlugin(), wallet, ticker, priceProvider.getCurrentData(ticker)
                ))
                .build();

    }

    @Override
    public void playOpenSound() {

    }
}
