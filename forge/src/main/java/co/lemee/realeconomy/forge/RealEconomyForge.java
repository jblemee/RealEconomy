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
package co.lemee.realeconomy.forge;

import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.forge.events.PlayerJoinEvent;
import co.lemee.realeconomy.forge.events.RegisterCommandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(RealEconomy.MOD_ID)
public class RealEconomyForge {
    public RealEconomyForge() {
        RealEconomy.init();
        MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent()); // Registers PlayerJoin event handler.
        MinecraftForge.EVENT_BUS.register(new RegisterCommandEvent()); // Registers PlayerJoin event handler.
    }
}