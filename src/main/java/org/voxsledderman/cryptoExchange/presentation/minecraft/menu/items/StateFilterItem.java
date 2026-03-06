package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.Menu;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.StateFilterItemProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.tittle.MenuType;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Objects;

@Getter
public class StateFilterItem extends AbstractItem {
    private final PositionState positionState;
    private final Wallet wallet;
    private final MenuFactory menuFactory;

    public StateFilterItem(PositionState currentState, Wallet wallet, MenuFactory menuFactory) {
        positionState = currentState;
        this.wallet = wallet;
        this.menuFactory = menuFactory;
    }


    @Override
    public ItemProvider getItemProvider(){
      return StateFilterItemProvider.createProvider(positionState)
;    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        PositionState newPositionState;
        MenuType menuType;

        if (Objects.requireNonNull(positionState) == PositionState.OPENED) {
            newPositionState = PositionState.CLOSED;
            menuType = MenuType.CLOSED_POSITIONS;
        } else {
            newPositionState = PositionState.OPENED;
            menuType = MenuType.OPENED_POSITIONS;
        }
        Menu menu = menuFactory.createPortfolioMenu(menuType, wallet, newPositionState);
        menu.openMenu(player);
    }
}
