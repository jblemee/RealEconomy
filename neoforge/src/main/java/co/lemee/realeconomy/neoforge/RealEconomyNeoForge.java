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
package co.lemee.realeconomy.neoforge;

import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.neoforge.events.PlayerJoinEvent;
import co.lemee.realeconomy.neoforge.events.RegisterCommandEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(RealEconomy.MOD_ID)
public class RealEconomyNeoForge {
    public RealEconomyNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        RealEconomy.init();
        NeoForge.EVENT_BUS.register(new PlayerJoinEvent()); // Registers PlayerJoin event handler.
        NeoForge.EVENT_BUS.register(new RegisterCommandEvent()); // Registers PlayerJoin event handler.
    }
}