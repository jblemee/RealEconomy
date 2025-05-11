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
package co.lemee.realeconomy.fabric.events;

import co.lemee.realeconomy.ErrorManager;
import co.lemee.realeconomy.events.PlayerJoinHandler;
import co.lemee.realeconomy.permission.PermissionManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Event listener that listens to for when a player joins and checks they have an account.
 * If the player doesn't have an account, one is made for them.
 */
public class PlayerJoinEvent implements ServerPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {

        new PlayerJoinHandler(handler.getPlayer().getName().getString(), handler.getPlayer().getUUID());

        // If the player has the "notify" perm, notify of any errors.
        if (PermissionManager.hasPermission(handler.getPlayer().getUUID(),
                PermissionManager.LOGIN_NOTIFY_PERMISSION)) {
            ErrorManager.printErrorsToPlayer(handler.getPlayer());
        }
    }
}
