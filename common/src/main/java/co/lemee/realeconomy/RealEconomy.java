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
package co.lemee.realeconomy;

import co.lemee.realeconomy.account.AccountManager;
import co.lemee.realeconomy.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point to the mod.
 */
public class RealEconomy {

    public static final String MOD_ID = "realeconomy";

    public static final String VERSION = "1.0.0"; // Mod version.
    public static final Logger LOGGER = LogManager.getLogger(); // Logger for logging to console.
    public static final String BASE_COMMAND = MOD_ID; // Base command for the mod.
    public static final String[] ALIASES = {"realeco", "reco"}; // Aliases of the base command.

    /**
     * Initializes the mod.
     */
    public static void init() {
        ConfigManager.loadConfig(); // Loads the config from file.
        AccountManager.initialise(); // Adds saved accounts to memory.
        LOGGER.info("RealEconomy Loaded.");
        ErrorManager.printErrorsToConsole();
    }
}
