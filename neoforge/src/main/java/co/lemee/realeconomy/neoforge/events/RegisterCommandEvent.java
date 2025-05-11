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
package co.lemee.realeconomy.neoforge.events;

import co.lemee.realeconomy.command.BaseCommand;
import co.lemee.realeconomy.command.commands.BalCommand;
import co.lemee.realeconomy.command.commands.BalTopCommand;
import co.lemee.realeconomy.command.commands.PayCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Class to register commands for forge.
 */
public class RegisterCommandEvent {

    /**
     * Method to register the commands on the forge event.
     * @param event The command register event.
     */
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        BalCommand.register(event.getDispatcher());
        BalTopCommand.register(event.getDispatcher());
        PayCommand.register(event.getDispatcher());
        BaseCommand.register(event.getDispatcher());
    }
}
