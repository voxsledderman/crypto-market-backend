package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers;

import org.bukkit.Material;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class WalletItemProvider {

    public static ItemProvider createProvider(PriceProvider priceProvider, MenuContext menuContext, Wallet wallet){
        var tickers = priceProvider.getFullMarketData(menuContext.getAppConfigManager().getTrackedTickers());
        String currentValue = PriceFormatter.formatMoney(
                WalletCalculator.getCurrentPortfolioValue(wallet, tickers));
        String roi = PriceFormatter.formatPercentage(WalletCalculator.getCurrentROI(wallet, tickers).toString());

        ItemBuilder builder = new ItemBuilder(Material.BOOK);
        builder.setDisplayName("Your portfolio");
        builder.addLoreLines(" ", "Portfolio value: %s (%s)".formatted(currentValue, roi));

        return builder;
    }
}
