package org.voxsledderman.cryptoExchange.presentation.minecraft.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.infrastructure.config.ConfigManager;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

@Getter
public abstract class Menu  {
   private final ConfigManager configManager;
   private Window window;

    public Menu(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public abstract String setupTitle();

   public abstract Gui setupGui();

   public abstract void playOpenSound();

    public void openMenu(Player player) {
        window = Window.single().setViewer(player).setGui(setupGui()).setTitle(setupTitle()).build();
        window.open();
        playOpenSound();
    }


}
