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
package co.lemee.realeconomy.fabric;

import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.fabric.events.PlayerJoinEvent;
import co.lemee.realeconomy.fabric.placeholder.PlaceholderRegister;
import co.lemee.realeconomy.fabric.registry.CommandRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class RealEconomyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RealEconomy.init();
        ServerPlayConnectionEvents.JOIN.register(new PlayerJoinEvent()); // Registers PlayerJoin event handler.
        CommandRegistry.registerCommands(); // Registers the commands.
        PlaceholderRegister.registerPlaceholders();
    }
}