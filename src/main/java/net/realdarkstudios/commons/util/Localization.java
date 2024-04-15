package net.realdarkstudios.commons.util;

import com.google.gson.JsonObject;
import net.realdarkstudios.commons.CommonsAPI;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.MissingFormatArgumentException;

public class Localization {
    private final Plugin plugin;
    private final LinkedHashMap<String, String> messages = new LinkedHashMap<>();
    private final RDSLogHelper logHelper;
    private final String prefix;

    /**
     * Creates a Localization based off of a {@link JsonObject}. Should never be invoked directly, instead use {@link LocalizationProvider#load(Plugin, Locale, String)}
     * @param json The JsonObject to load
     */
    Localization(Plugin plugin, JsonObject json, RDSLogHelper helper, String prefix) {
        this.plugin = plugin;
        for (String key: json.keySet()) {
            this.messages.put(key, json.get(key).toString());
        }
        this.logHelper = helper;
        this.prefix = prefix;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the prefix for this localization
     * @return The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the {@link RDSLogHelper} instance for this localization
     * @return The {@link RDSLogHelper}
     */
    public RDSLogHelper getLogHelper() {
        return logHelper;
    }

    /**
     * Gets the translation of the given path. No format arguments.
     * @param key The path of the translation
     * @return The translated message
     */
    public String getTranslation(String key) {
        if (!hasTranslation(key)) {
            if (!MessageKeys.TRANSLATION_MISSING.localizationHasPath()) return "Translation Not Found! (" + key + ")";
            else return String.format(MessageKeys.TRANSLATION_MISSING.translate(key));
        }
        String translation = messages.get(key);
        // The substring removes the quotation marks around the result
        return translation.substring(1, translation.length() - 1).replace("\\n", "\n").replace("\\\"", "\"");
    }

    /**
     * Gets the translation of the given path with format arguments
     * @param key The path of the translation
     * @param substitutions The format argument substitutions
     * @return The translated message
     */
    public String getTranslation(String key, Object... substitutions) {
        try {
            return String.format(getTranslation(key), substitutions);
        } catch (MissingFormatArgumentException e) {
            CommonsAPI.tWarning(MessageKeys.TRANSLATION_FORMAT_ARG_MISSING, key);
            return getTranslation(key);
        }
    }

    /**
     * Checks if a given translation exists
     * @param key The path of the translation
     * @return {@code true} if an entry exists, {@code false} if not
     */
    public boolean hasTranslation(String key) {
        return messages.containsKey(key);
    }

    public HashMap<String, String> getTranslations() {
        return messages;
    }
}
