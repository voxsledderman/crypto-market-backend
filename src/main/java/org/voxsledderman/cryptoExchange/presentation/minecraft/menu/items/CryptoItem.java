package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.formatters.PriceFormatter;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.List;

public class CryptoItem extends AbstractItem {

    private final CryptoInfo priceInfo;

    public CryptoItem(CryptoInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    @Override
    public ItemProvider getItemProvider(){
        return new ItemBuilder(Material.GOLD_NUGGET)
                .setDisplayName(priceInfo.fullName())
                .setLore(List.of(
                        new AdventureComponentWrapper(
                        Component.text("price: %s".formatted(PriceFormatter.formatMoney(priceInfo.price())))),
                        new AdventureComponentWrapper(
                                Component.text("24h change: %s".formatted(PriceFormatter.formatPercentage(priceInfo.changePercent())))
                        )
                ));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {

    }
}
