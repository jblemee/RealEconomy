/**
 * This file is part of RealEconomy.
 * <p>
 * RealEconomy is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * RealEconomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package co.lemee.realeconomy.command.commands;

import co.lemee.realeconomy.ErrorManager;
import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.account.AccountManager;
import co.lemee.realeconomy.command.SubCommandInterface;
import co.lemee.realeconomy.config.ConfigManager;
import co.lemee.realeconomy.permission.PermissionManager;
import co.lemee.realeconomy.util.Utils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Creates the command "/reco reload" in game.
 */
public class ReloadCommand implements SubCommandInterface {

    /**
     * Method used to add to the base command for this subcommand.
     * @return source to complete the command.
     */
    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("reload")
                .executes(this::run)
                .build();
    }

    /**
     * Method to perform the logic when the command is executed.
     * @param context the source of the command.
     * @return integer to complete command.
     */
    public int run(CommandContext<CommandSourceStack> context) {

        boolean isPlayer = context.getSource().isPlayer();
        ServerPlayer playerSource = context.getSource().getPlayer();

        // If the source is a player, check for a permission.
        if (isPlayer) {
            if (!PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.RELOAD_PERMISSION)) {
                context.getSource().sendSystemMessage(Component.literal("§cYou need the permission §b" +
                        PermissionManager.RELOAD_PERMISSION +
                        "§c to run this command."));
                return -1;
            }
        }

        // Tell the sender the mod is being reloaded.
        context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aReloading RealEconomy.",
                isPlayer)));

        // Reload the mod.
        ConfigManager.loadConfig(); // Loads the config from file.
        AccountManager.initialise(); // Adds saved accounts to memory.
        RealEconomy.LOGGER.info("RealEconomy reloaded.");
        ErrorManager.printErrorsToConsole();

        // If there are any errors, inform the player.
        if (ErrorManager.getErrors().size() != 0) {
            context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cErrors occurred while reloading.",
                    isPlayer)));
            if (isPlayer) {
                ErrorManager.printErrorsToPlayer(context.getSource().getPlayer());
            } else {
                ErrorManager.printErrorsToConsole();
            }
            return -1;
        }

        // Tell the player that the mod has been reloaded.
        context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aRealEconomy reloaded successfully.",
                isPlayer)));
        return 1;
    }
}
