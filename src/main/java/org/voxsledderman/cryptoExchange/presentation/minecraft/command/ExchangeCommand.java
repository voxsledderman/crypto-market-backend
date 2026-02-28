package org.voxsledderman.cryptoExchange.presentation.minecraft.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.infrastructure.config.ConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.MainMenu;

@Command(name = "exchange", aliases = {"ex", "giełda"})
public class ExchangeCommand {
    private final ConfigManager configManager;
    private final PriceProvider priceProvider;

    public ExchangeCommand(ConfigManager configManager, PriceProvider priceProvider) {
        this.configManager = configManager;
        this.priceProvider = priceProvider;
    }

    @Execute
    void openExchange(@Context CommandSender sender){
        if(sender instanceof Player player){
            MainMenu menu = new MainMenu(configManager, player, priceProvider);
            menu.openMenu(player);

        }
    }
}
