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

import co.lemee.realeconomy.account.Account;
import co.lemee.realeconomy.account.AccountManager;
import co.lemee.realeconomy.currency.Currency;
import co.lemee.realeconomy.permission.PermissionManager;
import co.lemee.realeconomy.util.Utils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Creates the command "/bal [player]" in game.
 */
public abstract class BalCommand {
    /**
     * Method to register and build the command.
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        createCommand(dispatcher);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        createCommand(dispatcher);
    }

    private static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> root = Commands
                .literal("balance")
                .executes(BalCommand::run)
                .then(Commands.argument("player", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            for (String name : ctx.getSource().getOnlinePlayerNames()) {
                                builder.suggest(name);
                            }
                            return builder.buildFuture();
                        })
                        .executes(BalCommand::run))
                .build();

        dispatcher.getRoot().addChild(root);

        // Adds alias bal to balance
        dispatcher.register(Commands.literal("bal").redirect(root).executes(BalCommand::run));
    }

    /**
     * Method that's used to execute the functionality for the command.
     * @param context the source of the command.
     * @return integer to complete the command.
     */
    public static int run(CommandContext<CommandSourceStack> context) {
        boolean isPlayer = context.getSource().isPlayer();
        ServerPlayer playerSource = context.getSource().getPlayer();

        // If the source is a player, check for permission.
        if (isPlayer) {
            if (!PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.BAL_PERMISSION)) {
                context.getSource().sendSystemMessage(Component.literal("§cYou need the permission §b" +
                        PermissionManager.BAL_PERMISSION +
                        "§c to run this command."));
                return -1;
            }
        }

        // Counts the amount of arguments given.
        int argLength = context.getInput().split(" ").length;

        Account account = null;

        // If only one argument is given (bal) then display the senders balances.
        if (argLength == 1) {
            account = AccountManager.getAccount(context.getSource().getPlayer().getUUID());
        }

        // If two arguments are given, get the balance of the player given and display it.
        if (argLength == 2) {
            String playerArg = StringArgumentType.getString(context, "player");

            // If the player doesn't have an account, tell the sender the player doesn't exist.
            if (!AccountManager.hasAccount(playerArg)) {
                context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
                        "exist.", isPlayer)));
                return -1;
            }

            account = AccountManager.getAccount(playerArg);
        }

        // Create the string to send back to the sender.
        String output = "§7=== §eBalances §7(§b" + account.getUsername() + "§7) ===\n\n";

        // Add each balance on the account to the string.
        for (Currency balance : account.getBalances().keySet()) {
            output += "§7- §b" + balance.getPlural() + "§7: §f" + account.getBalance(balance) + "\n";
        }

        // Send the balances to the sender.
        context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(output,
                isPlayer)));
        return 1;
    }
}
