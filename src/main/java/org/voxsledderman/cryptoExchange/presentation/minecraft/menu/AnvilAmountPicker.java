package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.MenuConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items.CryptoItem;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class AnvilAmountPicker{
    private final MenuFactory menuFactory;
    private final JavaPlugin plugin;
    private final MenuType menuType = MenuType.PICK_CRYPTO_AMOUNT;
    private final MenuConfigManager menuConfigManager;

    public AnvilAmountPicker(MenuFactory menuFactory, JavaPlugin plugin, MenuConfigManager menuConfigManager) {
        this.menuFactory = menuFactory;
        this.plugin = plugin;
        this.menuConfigManager = menuConfigManager;
    }

    public void openAnvilInputMenu(Player player, CryptoItem cryptoItem, Menu turnBackMenu, BigDecimal pickedAmount) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title(MiniMessage.miniMessage().serialize(menuConfigManager.getTitleByType(menuType)))
                .text(pickedAmount.toPlainString())
                .onClick((slot, stateSnapshot) -> handleAnvilClick(slot, stateSnapshot, player, cryptoItem, turnBackMenu))
                .open(player);
    }

    private List<AnvilGUI.ResponseAction> handleAnvilClick(int slot, AnvilGUI.StateSnapshot stateSnapshot,
                                                           Player player, CryptoItem cryptoItem, Menu turnBackMenu) {
        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

        String input = stateSnapshot.getText().replace(",", ".");
        if (!isBigDecimal(input)) return List.of(AnvilGUI.ResponseAction.replaceInputText("Invalid number!"));

        BigDecimal amount = new BigDecimal(input);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return List.of(AnvilGUI.ResponseAction.replaceInputText("Number must be greater than 0!"));
        }

        Wallet wallet = menuFactory.getMenuContext().getWalletRepository()
                .findByUuid(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("Wallet not found: " + player.getUniqueId()));

        player.sendMessage(Component.text("Set amount to: " + amount.toPlainString()).color(NamedTextColor.GREEN));
        menuFactory.createBuySellCryptoMenu(cryptoItem, amount, wallet, turnBackMenu).openMenu(player);
        return List.of(AnvilGUI.ResponseAction.close());
    }
    private boolean isBigDecimal(String string){
        if(string == null) return false;
        try{
            new BigDecimal(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
