package net.realdarkstudios.commons.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.realdarkstudios.commons.RDSCommons;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

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
     * @since 0.3.1.0-24w11a
     */
    public Localization load(Plugin plugin, Locale locale) {
        return pluginMap.containsKey(plugin) ? get(plugin) : loadPlugin(plugin, locale);
    }

    /**
     * Generates the {@link Localization} for this {@link Plugin} using the specified {@link Locale}
     * @param plugin The plugin to generate the Localization for
     * @param locale The Locale to use
     * @return A Localization which can be used for translation
     * @throws RuntimeException If a language can't be loaded for the given Plugin
     * @since 0.2.2.0
     */
    private Localization loadPlugin(Plugin plugin, Locale locale) {
        JsonObject json;

        // These all need to be basic strings due to the localization not being returned yet
        try {
            json = loadJson(plugin, locale);
            if (json.isJsonNull()) throw new Exception("JSON is null!");
            loadPluginMessage(plugin, locale, json);
        } catch (Exception e) {
            RDSCommons.warning("The Server Locale defined in config.yml is invalid or not supported! \nlang/" + locale.toLanguageTag() + ".json does not exist inside " + plugin.getName() + "'s resource folder! \nDefaulting to en-US.json!");
            e.printStackTrace();

            if (locale.equals(Locale.US)) {
                RDSCommons.warning(String.format("Unable to set a language for plugin: %s!", plugin.getName()));
                if (plugin.equals(RDSCommons.getInstance())) RDSCommons.getInstance().onDisable();
                else throw new RuntimeException("Could not load en-US.json for plugin " + plugin.getName());
                return null;
            }

            try {
                json = loadJson(plugin, Locale.US);
                loadPluginMessage(plugin, locale, json);
            } catch (Exception e1) {
                RDSCommons.warning(String.format("Unable to set a language for plugin: %s!", plugin.getName()));
                if (plugin.equals(RDSCommons.getInstance())) RDSCommons.getInstance().onDisable();
                else throw new RuntimeException("Could not load either " + locale.toLanguageTag() + ".json or en-US.json for plugin " + plugin.getName());
                return null;
            }
        }

        Localization localization = new Localization(json);
        pluginMap.put(plugin, localization);
        return localization;
    }

    private void loadPluginMessage(Plugin plugin, Locale locale, JsonObject json) {
        RDSCommons.info(String.format(plugin.equals(RDSCommons.getInstance()) ?
                        formatStr(json.get("plugin.localization.loaded").toString()) :
                        MessageKeys.LOCALIZATION_LOADED.getRawMessage(),
                json.has("locale.name") ? json.get("locale.name").toString() : localeNames.get(locale),
                json.has("plugin.name") ? json.get("plugin.name") + " (" + plugin.getName() + ")" : plugin.getName()));
    }

    /**
     * Loads the JSON String from "resources/lang/(locale).json" for a specific plugin Ex: resources/lang/en-US.json for US English
     * @param plugin The plugin to load the JSON from
     * @param locale The locale to load the JSON from
     * @return A JsonObject which can be used by the {@link Localization}
     * @throws IOException If no JSON could be read or if the file does not exist
     */
    private JsonObject loadJson(@NotNull Plugin plugin, @NotNull Locale locale) throws IOException {
        InputStreamReader reader = new InputStreamReader(plugin.getResource("lang/" + locale.toLanguageTag() + ".json"));
        if (!reader.ready()) {
            reader.close();
            throw new IOException("Reader not ready or JSON invalid for plugin " + plugin.getName());
        }

        JsonObject ret = gson.fromJson(reader, JsonObject.class);
        if (ret == null) throw new IOException("JSON could not be read from file " + plugin.getName() + "/resources/lang/" + locale.toLanguageTag() + ".json");
        else return ret;
    }

    /**
     * Gets the {@link Localization} for the given {@link Plugin}
     * @param plugin The plugin to get the Localization for
     * @return The requested Localization
     */
    public Localization get(Plugin plugin) {
        return pluginMap.get(plugin);
    }

    /**
     * Clears the LocalizationProvider's internal plugin map
     */
    public void clear() {
        this.pluginMap.clear();
    }

    /**
     * Gets the {@link LocalizationProvider} instance
     * @return The LocalizationProvider instance
     */
    public static LocalizationProvider getInstance() {
        if (RDSCommons.getAPILocalizationProvider() == null) return new LocalizationProvider();
        return RDSCommons.getAPILocalizationProvider();
    }

    private String formatStr(String str) {
        return str.substring(1, str.length() - 1).replace("\\n", "\n").replace("\\\"", "\"");
    }
}
