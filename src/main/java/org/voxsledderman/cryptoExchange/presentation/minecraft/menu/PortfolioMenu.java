package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.voxsledderman.cryptoExchange.domain.entities.TradeOrder;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.domain.services.WalletCalculator;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
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
    private final PositionState positionState;
    private final Menu turnBackMenu;
    private final MenuFactory menuFactory;

    public PortfolioMenu(MenuType menuType, Wallet wallet,
                         PositionState positionState, MenuFactory menuFactory) {
        super(menuType, menuFactory);
        this.wallet = wallet;
        this.positionState = positionState;
        this.menuFactory = menuFactory;
        this.turnBackMenu = new MainMenu(Bukkit.getPlayer(wallet.getOwnerUuid()), menuFactory);
    }

    @Override
    public Gui setupGui() {

        List<Item> items = new ArrayList<>();
        Map<String, List<TradeOrder>> ordersMap = wallet.getOrders();

        ordersMap.keySet()
                .forEach(ticker -> {
                    if(WalletCalculator.getTotalAmountOfCryptoAcquired(wallet, ticker, positionState).compareTo(BigDecimal.ZERO) > 0) {
                        items.add(new TradeItem(wallet, ticker, positionState, getMenuFactory()));
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
                .addIngredient('P', new StateFilterItem(positionState, wallet, menuFactory))
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
