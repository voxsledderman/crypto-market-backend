package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.infrastructure.config.ConfigManager;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.CloseItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.CryptoItem;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu extends Menu{
    private final Player player;
    private final PriceProvider priceProvider;
    public MainMenu(ConfigManager configManager, Player player, PriceProvider priceProvider) {
        super(configManager);
        this.player = player;
        this.priceProvider = priceProvider;
    }

    @Override
    public String setupTitle() {
        return "test";
    }

    @Override
    public Gui setupGui() {
        List<Item> cryptoItems = new ArrayList<>();
        Map<String, CryptoInfo> map = priceProvider.getFullMarketData(getConfigManager().getTrackedTickers());

        for(String key : map.keySet()){
            cryptoItems.add(new CryptoItem(new CryptoInfo(map.get(key).fullName(), map.get(key).price(), map.get(key).changePercent())));
        }
        player.sendMessage(getConfigManager().getTrackedTickers().toString());
        player.sendMessage(map.keySet().toString());
        player.sendMessage(map.values().toString());

        Gui gui = PagedGui.items()
                .setStructure(
                        "P . . . . . . . .",
                        ". . C C C C C . .",
                        "B . C C C C C . N",
                        ". . C C C C C . .",
                        ". . C C C C C . .",
                        "E . . . . . . . ."
                )
                .addIngredient('C', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('E', new CloseItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("Close menu")))
                .setContent(cryptoItems)
                .build();
        return gui;
    }

    @Override
    public void playOpenSound() {

    }
}
