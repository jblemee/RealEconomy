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
package co.lemee.realeconomy.fabric.registry;

import co.lemee.realeconomy.command.BaseCommand;
import co.lemee.realeconomy.command.commands.BalCommand;
import co.lemee.realeconomy.command.commands.BalTopCommand;
import co.lemee.realeconomy.command.commands.PayCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public abstract class CommandRegistry {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BaseCommand::register);
        CommandRegistrationCallback.EVENT.register(PayCommand::register);
        CommandRegistrationCallback.EVENT.register(BalCommand::register);
        CommandRegistrationCallback.EVENT.register(BalTopCommand::register);
    }
}
