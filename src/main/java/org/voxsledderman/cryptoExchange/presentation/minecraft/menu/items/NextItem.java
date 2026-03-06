package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.PageItemProvider;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class NextItem extends PageItem {

    public NextItem(boolean forward) {
        super(forward);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        return PageItemProvider.createNextItemProvider(gui);
    }
}
