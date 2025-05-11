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
package co.lemee.realeconomy.config;

import co.lemee.realeconomy.ErrorManager;
import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.util.Utils;

/**
 * Manager class to load, write and store the config.
 */
public abstract class ConfigManager {
    // The field that will store the config.
    private static Config realEcoConfig;

    /**
     * Method to load the config and store it into the realEcoConfig field.
     */
    public static void loadConfig() {

        Config cfg = null;

        // Read the config from file.
        cfg = Utils.readFromFile("", "config", Config.class);

        // If there is no config in file, create a new one.
        if (cfg == null) {
            RealEconomy.LOGGER.info("Couldn't find config for RealEconomy, creating new config.");
            boolean success = Utils.writeToFile("", "config", new Config());

            // If writing the file failed, give an error.
            if (!success) {
                ErrorManager.addError("Failed to create config. Please check the console for any errors.");
                return;
            }

            // Read the config from a file.
            cfg = Utils.readFromFile("", "config", Config.class);
        }


        // Check the config is valid.
        boolean hasPassed = Utils.checkConfig(cfg);

        // If the config isn't valid, give an error.
        if (!hasPassed) {
            ErrorManager.addError("Config is invalid. Please regenerate or fix the errors.");
        }

        realEcoConfig = cfg;
    }

    /**
     * getter for the config.
     * @return The config being used.
     */
    public static Config getConfig() {
        return ConfigManager.realEcoConfig;
    }

}
