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
package co.lemee.realeconomy.placeholder;

import co.lemee.realeconomy.account.AccountManager;
import co.lemee.realeconomy.config.ConfigManager;
import co.lemee.realeconomy.currency.Currency;

import java.util.UUID;

/**
 * Class used to grab placeholder values.
 */
public abstract class Placeholders {

    /**
     * Method to get the default balance of the player.
     * @param player The player to get the balance of.
     * @return float that represents the value of the players balance.
     */
    public static float getDefaultBalance(UUID player) {
        Currency defaultCurrency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());

        return AccountManager.getAccount(player).getBalance(defaultCurrency);
    }

    /**
     * Method to get the balance of the player and currency given.
     * @param player The player to get the balance of.
     * @param currency The currency to get the balance of.
     * @return float that represents the currency of the player.
     */
    public static float getBalance(UUID player, Currency currency) {
        return AccountManager.getAccount(player).getBalance(currency);
    }


}
