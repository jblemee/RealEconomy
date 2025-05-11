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
package co.lemee.realeconomy.events;

import co.lemee.realeconomy.account.Account;
import co.lemee.realeconomy.account.AccountManager;

import java.util.UUID;

public class PlayerJoinHandler {

    public PlayerJoinHandler(String username, UUID uuid) {
        // If the player has changed their name, update it.
        if (!AccountManager.hasAccount(username) &&
                AccountManager.hasAccount(uuid)) {
            Account account = AccountManager.getAccount(uuid);
            account.changeUsername(username);
            AccountManager.updateAccount(account);
        }

        // If the player doesn't have an account, create one.
        if (!AccountManager.hasAccount(uuid)) {
            AccountManager.createAccount(uuid, username);
        }
    }
}
