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
package co.lemee.realeconomy.fabric.placeholder;

import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.config.ConfigManager;
import co.lemee.realeconomy.currency.Currency;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.resources.ResourceLocation;

/**
 * Registry for Placeholders on Fabric
 */
public abstract class PlaceholderRegister {
    /**
     * Method to register all placeholders.
     */
    public static void registerPlaceholders() {

        // Register all placeholders for currencies "realeconomy:currency_[currency name]"
        for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
            Placeholders.register(ResourceLocation.tryBuild(RealEconomy.MOD_ID, "currency_" + currency.getName()),
                    (ctx, arg) -> {
                        if (!ctx.hasPlayer()) {
                            return PlaceholderResult.invalid("No Player!");
                        }
                        float balance = co.lemee.realeconomy.placeholder.Placeholders.getBalance(ctx.player().getUUID(), currency);

                        return PlaceholderResult.value(String.valueOf(balance));
                    });
        }

        // Register a placeholder for the default currency "realeconomy:currency"
        Placeholders.register(ResourceLocation.tryBuild(RealEconomy.MOD_ID, "currency"),
                (ctx, arg) -> {
                    if (!ctx.hasPlayer()) {
                        return PlaceholderResult.invalid("No Player!");
                    }
                    float balance = co.lemee.realeconomy.placeholder.Placeholders.getDefaultBalance(ctx.player().getUUID());

                    return PlaceholderResult.value(String.valueOf(balance));
                });
    }

}
