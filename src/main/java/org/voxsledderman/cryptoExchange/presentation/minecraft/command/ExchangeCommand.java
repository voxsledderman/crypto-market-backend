package org.voxsledderman.cryptoExchange.presentation.minecraft.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuFactory;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.MainMenu;

@Command(name = "exchange", aliases = {"ex", "giełda"})
public class ExchangeCommand {
    private final MenuFactory menuFactory;
    public ExchangeCommand(MenuFactory menuFactory) {
        this.menuFactory = menuFactory;
    }

    @Execute
    void openExchange(@Context CommandSender sender){
        if(sender instanceof Player player){
            MainMenu menu = new MainMenu(player, menuFactory);
            menu.openMenu(player);

        }
    }
}
