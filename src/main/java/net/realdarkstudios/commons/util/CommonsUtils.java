package net.realdarkstudios.commons.util;

import net.realdarkstudios.commons.CommonsAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommonsUtils {
    /**
     * The placeholder {@link UUID} in string form
     */
    public static final String EMPTY_UUID_STRING = "00000000-0000-0000-0000-000000000000";
    /**
     * The placeholder {@link UUID}
     */
    public static final UUID EMPTY_UUID = UUID.fromString(EMPTY_UUID_STRING);

    /**
     * Generates a random string ([0-9A-Z]) of the given length
     * @param length How long the string should be
     * @return The randomly generated cache ID
     */
    public static String generateRandomString(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int n = alphabet.length();

        StringBuilder result = new StringBuilder();
        Random r = new Random();

        for (int i=0; i<length; i++)
            result.append(alphabet.charAt(r.nextInt(n)));

        return result.toString();
    }

    /**
     * Compares a {@link HashMap}  by the given comparator
     * @param hashMap the hashmap
     * @return The sorted hashmap
     */
    public static <I, V> HashMap<I, V> sortHashMap(HashMap<I, V> hashMap, Comparator<? super Map.Entry<I, V>> comparator) {
        List<Map.Entry<I, V>> list = new LinkedList<>(hashMap.entrySet());
        list.sort(comparator);

        HashMap<I, V> sorted = new LinkedHashMap<>(hashMap.size());
        for (Map.Entry<I, V> entry: list) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }

    /**
     * Gets the name of a player, or [CONSOLE] if it is not a player
     * @param commandSender The {@link CommandSender} to get the name of
     * @return The command sender name
     */
    public static String commandSenderName(CommandSender commandSender) {
        return commandSender instanceof Player plr ? plr.getDisplayName() : "[CONSOLE]";
    }

    /**
     * Gets the name of a player, or [CONSOLE] if it is not a player (usually for Minecaching this will be the result of using {@link CommonsUtils#EMPTY_UUID})
     * @param uuid The {@link UUID} to get the name of
     * @return The UUID name
     */
    public static String uuidName(UUID uuid) {
        return uuid.equals(EMPTY_UUID) ? "[CONSOLE]" : CommonsAPI.get().hasPlayerData(uuid) ? CommonsAPI.get().getPlayerData(uuid).getUsername() : Bukkit.getOfflinePlayer(uuid).getName();
    }
}
