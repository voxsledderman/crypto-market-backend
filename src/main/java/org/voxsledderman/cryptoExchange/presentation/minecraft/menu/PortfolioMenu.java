package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.*;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortfolioMenu extends Menu{
    private final Wallet wallet;
    private final PriceProvider priceProvider;
    private final PositionState positionState;
    private final Menu turnBackMenu;

    public PortfolioMenu(MenuType menuType, Wallet wallet, PriceProvider priceProvider,
                         PositionState positionState, MenuContext menuContext, JavaPlugin plugin) {
        super(plugin, menuContext, menuType);
        this.wallet = wallet;
        this.priceProvider = priceProvider;
        this.positionState = positionState;
        this.turnBackMenu = new MainMenu(menuContext, Bukkit.getPlayer(wallet.getOwnerUuid()),priceProvider, plugin);
    }

    @Override
    public Gui setupGui() {

        List<Item> items = new ArrayList<>();
        Map<String, List<TradeOrder>> ordersMap = wallet.getOrders();

        var marketData = priceProvider.getFullMarketData(new ArrayList<>(ordersMap.keySet())); //TODO: test if possible

        ordersMap.keySet()
                .forEach(ticker -> {
                    if(WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, ticker, positionState).compareTo(BigDecimal.ZERO) > 0) {
                        items.add(new TradeItem(wallet, ticker, positionState, getMenuContext(), priceProvider, getPlugin()));
                    }
                });

        return PagedGui.items()
                .setStructure(
                        "P . . . . . . . E",
                        ". b T T T T T b .",
                        "< . T T T T T . >",
                        ". b T T T T T b .",
                        "B . . . . . . . w"
                )
                .addIngredient('T', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('>', new NextItem(true))
                .addIngredient('<', new BackItem(false))
                .addIngredient('P', new StateFilterItem(positionState, wallet, priceProvider, getMenuContext(), getPlugin()))
                .addIngredient('w', new SimpleItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)))
                .addIngredient('b', new SimpleItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
                .addIngredient('B', new TurnBackItem(turnBackMenu))
                .addIngredient('E', new CloseItem())
                .setContent(items)
                .build();
    }

    @Override
    public void playOpenSound() {

    }
}
