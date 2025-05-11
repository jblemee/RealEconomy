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

import co.lemee.realeconomy.command.SubCommandInterface;
import co.lemee.realeconomy.permission.PermissionManager;
import co.lemee.realeconomy.util.Utils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Creates the command "/reco add" in game.
 */
public class HelpCommand implements SubCommandInterface {

    /**
     * Method used to add to the base command for this subcommand.
     * @return source to complete the command.
     */
    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("help").executes(this::run).build();
    }

    /**
     * Method to perform the logic when the command is executed.
     * @param context the source of the command.
     * @return integer to complete command.
     */
    public int run(CommandContext<CommandSourceStack> context) {

        boolean isPlayer = context.getSource().isPlayer();
        ServerPlayer playerSource = context.getSource().getPlayer();

        // Create the output string.
        String usage = "§2RealEconomy Commands:\n";

        // If the source is a player, check for a permission to view admin commands.
        if (isPlayer) {
            if (PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.HELP_EXTRAS_PERMISSION)) {
                usage = usage + adminHelp(); // Add the admin commands to the output string.

            }
        } else {
            // If the sender isn't a payer, add the admin commands to the output string.
            usage = usage + adminHelp();
        }
        // Add standard commands to the output string.
        usage = usage +
                "> /reco help\n" +
                "> /pay <player> <amount> [currency]\n" +
                "> /bal [player]\n" +
                "> /baltop [currency]";

        // Send the output string to the player.
        context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(usage, isPlayer)));
        return 1;
    }

    /**
     * Method to store the admin help string in.
     * @return String that holds the admin commands.
     */
    private String adminHelp() {
        return "> /reco add <player> <currency> <amount>\n" +
                "> /reco remove <player> <currency> <amount>\n" +
                "> /reco set <player> <currency> <amount>\n" +
                "> /reco clear <player> <currency>\n" +
                "> /reco reload\n";
    }
}
