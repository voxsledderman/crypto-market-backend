package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.PageItemProvider;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class BackItem extends PageItem {
    public BackItem(boolean forward) {
        super(forward);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        return PageItemProvider.createBackItemProvider(gui);
    }
}
