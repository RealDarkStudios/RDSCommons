package net.realdarkstudios.commons.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.RDSCommons;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class LocalizationProvider {
    private final Gson gson = new Gson();
    private final HashMap<Plugin, Localization> pluginMap = new HashMap<>();
    private final HashMap<Locale, String> localeNames = new HashMap<>();
    private LocalizationProvider() {
        localeNames.put(Locale.US, "US English");
    }

    /**
     * Generates the {@link Localization} for this {@link Plugin} with the defined {@link Locale}
     * @param plugin The plugin to generate the Localization for
     * @param locale The Locale to use
     * @return A Localization which can be used for translation
     * @since 1.0.0.0
     */
    public Localization load(Plugin plugin, Locale locale, String prefix) {
        return pluginMap.containsKey(plugin) ? get(plugin) : loadPlugin(plugin, locale, prefix);
    }

    /**
     * Generates the {@link Localization} for this {@link Plugin} using the specified {@link Locale}
     * @param plugin The plugin to generate the Localization for
     * @param locale The Locale to use
     * @return A Localization which can be used for translation
     * @throws RuntimeException If a language can't be loaded for the given Plugin
     * @since 1.0.0.0
     */
    private Localization loadPlugin(Plugin plugin, Locale locale, String prefix) {
        JsonObject json;

        // These all need to be basic strings due to the localization not being returned yet
        try {
            json = loadJson(plugin, locale);
            if (json.isJsonNull()) throw new Exception("JSON is null!");
            loadPluginMessage(plugin, locale, json);
        } catch (Exception e) {
            e.printStackTrace();
            CommonsAPI.warning("Defaulting to 'en-US'!");

            if (locale.equals(Locale.US)) {
                CommonsAPI.warning(String.format("Unable to create localization '%s' for plugin: '%s'!", locale.toLanguageTag(), plugin.getName()));
                if (plugin.equals(RDSCommons.getInstance())) RDSCommons.getInstance().onDisable();
                else throw new RuntimeException("Could not load localization 'en-US' for plugin '" + plugin.getName() + "'");
                return null;
            }

            try {
                json = loadJson(plugin, Locale.US);
                loadPluginMessage(plugin, locale, json);
            } catch (Exception e1) {
                CommonsAPI.warning(String.format("Unable to create localization '%s' for plugin: '%s'!", locale.toLanguageTag(), plugin.getName()));
                if (plugin.equals(RDSCommons.getInstance())) RDSCommons.getInstance().onDisable();
                else throw new RuntimeException("Could not load either localization ('" + locale.toLanguageTag() + "' and 'en-US') for plugin '" + plugin.getName() + "'");
                return null;
            }
        }

        Localization localization = new Localization(plugin, json, new RDSLogHelper(plugin.getLogger()), prefix);
        pluginMap.put(plugin, localization);
        return localization;
    }

    /**
     * Helper function to print the loaded localization for plugin message
     * @param plugin The {@link Plugin}
     * @param locale The {@link Locale}
     * @param json The {@link JsonObject}
     * @since 1.0.0.0
     */
    private void loadPluginMessage(Plugin plugin, Locale locale, JsonObject json) {
        CommonsAPI.info(String.format(plugin.equals(RDSCommons.getInstance()) ?
                        formatStr(json.get("api.loaded_localization").toString()) :
                        MessageKeys.Api.LOADED_LOCALIZATION.getRawMessage(),
                json.has("locale.name") ? json.get("locale.name").toString().replace("\"", "") : localeNames.get(locale),
                json.has("plugin.name") ? json.get("plugin.name").toString().replace("\"", "") : plugin.getName(),
                plugin.getName()));
    }

    /**
     * Loads the JSON String from "resources/lang/(locale).json" for a specific plugin Ex: resources/lang/en-US.json for US English
     * @param plugin The plugin to load the JSON from
     * @param locale The locale to load the JSON from
     * @return A JsonObject which can be used by the {@link Localization}
     * @throws IOException If no JSON could be read or if the file does not exist
     * @since 1.0.0.0
     */
    private JsonObject loadJson(@NotNull Plugin plugin, @NotNull Locale locale) throws IOException {
        InputStream stream;

        try {
            stream = plugin.getClass().getClassLoader().getResourceAsStream("lang/" + locale.toLanguageTag() + ".json");
            if (stream == null) throw new Exception();
        } catch (Exception e) {
            throw new IOException("Could not get resource " + plugin.getName() + "/resources/lang/" + locale.toLanguageTag() + ".json!");
        }

        InputStreamReader reader = new InputStreamReader(stream);
        if (!reader.ready()) {
            reader.close();
            throw new IOException("Reader not ready or JSON invalid for plugin " + plugin.getName());
        }

        JsonObject ret = gson.fromJson(reader, JsonObject.class);
        reader.close();

        if (ret == null) throw new IOException("JSON could not be read from file " + plugin.getName() + "/resources/lang/" + locale.toLanguageTag() + ".json");
        else return ret;
    }

    /**
     * Gets the {@link Localization} for the given {@link Plugin}
     * @param plugin The plugin to get the Localization for
     * @return The requested Localization
     * @since 1.0.0.0
     */
    public Localization get(Plugin plugin) {
        return pluginMap.get(plugin);
    }

    /**
     * Clears the LocalizationProvider's internal plugin map
     * @since 1.0.0.0
     */
    public void clear() {
        this.pluginMap.clear();
    }

    public HashMap<String, Localization> getAllTranslations() {
        HashMap<String, Localization> ret = new HashMap<>();

        for (Localization localization : pluginMap.values()) {
            Set<String> keys = localization.getTranslations().keySet();
            HashMap<String, Localization> fixedKeys = new HashMap<>();
            keys.forEach(key -> fixedKeys.put(localization.getPlugin().getName().toLowerCase() + ":" + key, localization));
            ret.putAll(fixedKeys);
        }

        return ret;
    }

    /**
     * Gets the {@link LocalizationProvider} instance
     * @return The LocalizationProvider instance
     * @since 1.0.0.0
     */
    public static LocalizationProvider getInstance() {
        if (RDSCommons.getAPI().getLocalizationProvider() == null) return new LocalizationProvider();
        return RDSCommons.getAPI().getLocalizationProvider();
    }

    private String formatStr(String str) {
        return str.substring(1, str.length() - 1).replace("\\n", "\n").replace("\\\"", "\"");
    }
}
