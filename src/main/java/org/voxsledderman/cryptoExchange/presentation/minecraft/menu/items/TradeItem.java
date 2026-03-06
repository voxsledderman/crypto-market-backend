package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.BuySellCryptoMenu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.PortfolioMenu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeItem extends AutoUpdateItem {
    private final Wallet wallet;
    private final String ticker;
    private final MenuContext menuContext;
    private final PriceProvider priceProvider;
    private final JavaPlugin plugin;



    public TradeItem(Wallet wallet, String ticker, PositionState positionState, MenuContext menuContext, PriceProvider priceProvider, JavaPlugin plugin) {
        super(3 * 20, () -> {
            CryptoInfo cryptoInfo = priceProvider.getCurrentData(ticker);
            return getProvider(wallet, ticker, cryptoInfo, positionState);
        });
        this.wallet = wallet;
        this.menuContext = menuContext;
        this.priceProvider = priceProvider;
        this.plugin = plugin;
        this.ticker = ticker;
    }


    public static ItemProvider getProvider(Wallet wallet, String ticker, CryptoInfo cryptoInfo, PositionState positionState){
        String cryptoValue = PriceFormatter.formatMoney(WalletCalculator.getSingleCryptoValue(wallet, ticker, cryptoInfo));
        String roi = PriceFormatter.formatPercentage(WalletCalculator.getSingleCryptoROI(wallet, ticker, cryptoInfo).toString());
        AtomicInteger temp = new AtomicInteger(1);

        ItemBuilder builder = new ItemBuilder(Material.BOOK);
        builder.setDisplayName(cryptoInfo.fullName() + " x"  + WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, ticker, positionState));
        builder.addLoreLines(
                "total value: %s - (%s)".formatted(
                       cryptoValue , roi));
        wallet.getOrders().get(ticker)
                .forEach(
                        trade -> {
                            if(trade.getPositionState().equals(positionState)) {
                                var currentValue = trade.getTradeValueNow(cryptoInfo.price());
                                builder.addLoreLines(
                                        "%s. %s x%s %s >> %s".formatted(temp.getAndIncrement(), cryptoInfo.fullName(), trade.getAmount() ,PriceFormatter.formatMoney(currentValue)
                                                , PriceFormatter.formatPercentage(trade.getProfit(currentValue.divide(trade.getAmount(), RoundingMode.HALF_DOWN)).toString()))
                                );
                            }
                        }
                );
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
       Menu menu = new BuySellCryptoMenu(MenuType.BUY_OR_SELL_CRYPTO, new CryptoItem(
               ticker, priceProvider, menuContext, plugin), BigDecimal.ZERO, priceProvider, wallet,
               new PortfolioMenu(MenuType.OPENED_POSITIONS, wallet, priceProvider, PositionState.OPENED, menuContext, plugin)
               );
       menu.openMenu(player);
    }
}
