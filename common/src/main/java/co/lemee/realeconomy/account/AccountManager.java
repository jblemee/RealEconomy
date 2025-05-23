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
package co.lemee.realeconomy.account;

import co.lemee.realeconomy.ErrorManager;
import co.lemee.realeconomy.RealEconomy;
import co.lemee.realeconomy.currency.Currency;
import co.lemee.realeconomy.util.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.util.*;

import static co.lemee.realeconomy.util.Utils.readFileAsync;

/**
 * AccountManager class to store the accounts of all players.
 * Allows creation, updating and retrieving accounts.
 */
public abstract class AccountManager {
    private static final HashMap<String, Account> accounts = new HashMap<>(); // Memory storage for accounts.

    /**
     * Method that checks a user has an account using the username.
     * @param username the username of the player to check.
     * @return true if the player has an account.
     */
    public static boolean hasAccount(String username) {
        return accounts.containsKey(username.toLowerCase());
    }

    /**
     * Method that checks a user has an account using the UUID.
     * @param uuid the UUID of the player to check.
     * @return true if the player has an account.
     */
    public static boolean hasAccount(UUID uuid) {
        for (Account account : accounts.values()) {
            if (account.getUUID().equals(uuid)) return true;
        }
        return false;
    }

    /**
     * Method to retrieve the account of a player.
     * @param username username of the players account to retrieve.
     * @return the account of the player requested.
     */
    public static Account getAccount(String username) {
        return accounts.get(username.toLowerCase());
    }

    /**
     * Gets the account of a player.
     * @param uuid uuid of the players account to retrieve.
     * @return the account of the player.
     */
    public static Account getAccount(UUID uuid) {
        for (Account account : accounts.values()) {
            if (account.getUUID().equals(uuid)) return account;
        }
        return null;
    }

    /**
     * Method to get all accounts in memory.
     * @return ArrayList that holds all players accounts.
     */
    public static ArrayList<Account> getAllAccounts() {
        return (ArrayList<Account>) accounts.values();
    }


    /**
     * Method to update an account with the one provided.
     * @param account the account that should be updated.
     * @return true if the account was successfully updated.
     */
    public static boolean updateAccount(Account account) {
        // Find the existing key case-insensitively for removal
        String oldKey = null;
        for (String key : accounts.keySet()) {
            if (key.equalsIgnoreCase(account.getUsername())) {
                oldKey = key;
                break;
            }
        }

        Account oldAccount = null;
        if (oldKey != null) {
            oldAccount = accounts.remove(oldKey);
        }
        
        // If oldAccount is null and we are trying to update, it might be a new account or a name change scenario.
        // For a name change, the UUID would match. Let's assume 'account' has the new username.
        // We'll put the account with the new username, lowercased.

        accounts.put(account.getUsername().toLowerCase(), account);
        Gson gson = Utils.newGson();
        boolean success = Utils.writeFileAsync("accounts/", account.getUUID().toString() + ".json",
                gson.toJson(new AccountFile(account)));
        if (!success) {
            // Rollback
            accounts.remove(account.getUsername().toLowerCase());
            if (oldAccount != null && oldKey != null) { // If we successfully removed an old entry
                accounts.put(oldKey, oldAccount);
            }
            ErrorManager.addError("Failed to write account to storage for account: " + account.getUsername());
            RealEconomy.LOGGER.error("Failed to write account to storage for account: " + account.getUsername());
            return false;
        }

        return true;
    }

    /**
     * Method to create a new account for a player.
     * @param uuid the UUID of the player to create the account for.
     * @param username the username of the player.
     * @return the create account.
     */
    public static boolean createAccount(UUID uuid, String username) {
        String lowerCaseUsername = username.toLowerCase();
        accounts.put(lowerCaseUsername, new Account(uuid, username)); // Store original case username in Account object
        Gson gson = Utils.newGson();
        // Use the username from the account object for AccountFile, which might have original casing
        boolean success = Utils.writeFileAsync("accounts/", getAccount(lowerCaseUsername).getUUID().toString() + ".json",
                gson.toJson(new AccountFile(accounts.get(lowerCaseUsername))));
        if (!success) {
            accounts.remove(lowerCaseUsername);
            ErrorManager.addError("Failed to write account to storage for account: " + username);
            RealEconomy.LOGGER.error("Failed to write account to storage for account: " + username);
            return false;
        }
        return true;
    }

    /**
     * Gets all sorted account balances of a given currency.
     * @param currency the currency to sort by.
     * @return ArrayList that holds a lists containing a username and balance.
     */
    public static List<Account> sortAccountsByBalance(Currency currency) {

        Comparator<Account> currencyComparator = ((ac1, ac2) -> Float.compare(ac1.getBalance(currency), ac2.getBalance(currency)));

        List<Account> sortedAccounts = accounts.values().
                stream().
                sorted(currencyComparator.reversed()).toList();

        return sortedAccounts;
    }

    /**
     * Method to initialise the AccountManager when the server starts.
     */
    public static void initialise() {
        try {
            File dir = Utils.checkForDirectory(Utils.BASE_PATH + "accounts/");

            String[] list = dir.list();

            // If no files, return.
            if (list == null || list.length == 0) { // Added null check for list
                return;
            }

            for (String file : list) {
                // Read the file, convert it AccountFile object and then load the account to memory.
                readFileAsync("accounts/", file, (el -> {
                    Gson gson = Utils.newGson();
                    AccountFile accountFile = gson.fromJson(el, AccountFile.class);
                    // Add the account to accounts hashmap.
                    // Use toLowerCase for the key, but Account object stores original username from file
                    accounts.put(accountFile.getUsername().toLowerCase(), new Account(accountFile));
                }));
            }
        } catch (Exception e) {
            // It's good practice to log the exception or handle it more gracefully
            RealEconomy.LOGGER.error("Error during AccountManager initialisation: ", e);
        }
    }
}
