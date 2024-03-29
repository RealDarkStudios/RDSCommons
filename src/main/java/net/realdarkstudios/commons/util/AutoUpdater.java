package net.realdarkstudios.commons.util;

import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.RDSCommons;
import net.realdarkstudios.commons.misc.IRDSPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AutoUpdater {
    private final Plugin plugin;
    private final String dataURL, versionURL;
    private Parser parser;
    private Map<String, String> replacements = new HashMap<>();
    private static String newVer = "", branch = "";
    private static boolean doUpdate = false;
    private static int lastUpdateCheck = 10000;

    /**
     * Creates an auto-updater for a given Plugin.
     * There is no need to create more than one auto updater, as this may have bad consequences.
     * @param plugin The {@link Plugin} that owns this auto updater.
     * @param dataURL The URL to check for updates from. Put '%b%' wherever the branch would go in the URL.
     * @param versionURL The URL to grab a version file from. Put '%b%' where the branch would go, and '%v%' where the version would go.
     * @param parser The {@link Parser} to use, which determines how to parse the URL data. For a CUSTOM parser type, use Parser.CUSTOM.setParser() to set your custom parsing method
     */
    public AutoUpdater(Plugin plugin, String dataURL, String versionURL, Parser parser) {
        this.plugin = plugin;
        this.dataURL = dataURL;
        this.versionURL = versionURL;
        this.parser = parser;
    }

    /**
     * Gets the parser type of the current {@link AutoUpdater}
     * @return The Parser
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Sets the parser.
     * @param parser The custom parsing method, a {@link Function} of {@link URL} that returns a {@link HashMap} of {@link Version} and {@link String}
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    /**
     * Gets the map of replacements
     * @return The {@link Map} with all replacements
     */
    public Map<String, String> getReplacements() {
        return replacements;
    }

    /**
     * Sets the map of replacements.
     * For example, if you have a version that will not be correctly parsed, you can set a replacement for it.
     * <br>
     * <br>
     * (snapshot-24w13a in this case is something that will not get parsed)
     * <br>
     * Ex. Putting in {@code ("snapshot-24w13a", "1.0.0.0-snapshot-24w13a")} will plug in {@code "1.0.0.0-snapshot-24w13a"} into the {@code Version#fromString} call, which will be parsed.
     * @param replacements The map of replacements
     */
    public void setReplacements(Map<String, String> replacements) {
        this.replacements = replacements;
    }

    /**
     * Updates the branch of the readURL
     * @param newBranch which branch to use
     * @since 0.3.0.1
     */
    public void updateBranch(String newBranch) {
        branch = newBranch;
    }

    /**
     * Check if there is an update that needs to be downloaded/installed
     * @return {@code true} if there is a newer version available for download, {@code false} if not
     * @since 0.3.0.0
     */
    public boolean hasUpdate() {
        return doUpdate;
    }

    /**
     * Gets the new version string
     * @return The newest version
     * @since 0.3.0.0
     */
    public String getNewestVersion() {
        return newVer;
    }

    /**
     * Returns the result of the last update check
     * @return -1 if plugin version is BEHIND, 0 if it is UP-TO-DATE, 1 if it is AHEAD, and 10000 for ERROR
     */
    public int getLastCheckResult() {
        return lastUpdateCheck;
    }

    public void checkForUpdate() {
        CommonsAPI.tInfo(MessageKeys.Update.CHECKING, plugin.getName(), parser.getName());

        final int[] result = new int[1];
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<Version, String> versionMap;

                try {
                    // Grab the info from maven-metadata.xml on the maven
                    URL readURL = new URL(dataURL.replace("%b%", branch));

                    versionMap = parser.getParser().apply(readURL, replacements);

                    // Put all versions into an array
                    List<Version> versions = new ArrayList<>(versionMap.keySet());

                    if (versions.isEmpty()) {
                        CommonsAPI.tInfo(MessageKeys.Update.NO_VERSIONS_AVAILABLE);
                        result[0] = 10000;
                        return;
                    }

                    // Sort accoring to Version.fromString()
                    versions.sort(Version::compareTo);

                    // Get the most recent version
                    Version laterV = versions.get(versions.size() - 1);
                    CommonsAPI.tInfo(MessageKeys.Update.LATEST, branch, plugin.getName(), versionMap.get(laterV));

                    Version pluginVersion;

                    if (plugin instanceof IRDSPlugin p) {
                        pluginVersion = p.getVersion();
                    } else {
                        try {
                            pluginVersion = Version.fromString(plugin.getDescription().getVersion());
                        } catch (Exception e) {
                            CommonsAPI.tWarning(MessageKeys.Update.FAIL_TO_PARSE_VERSION, plugin.getDescription().getVersion());
                            result[0] = 10000;
                            errorChecking();
                            return;
                        }
                    }

                    // Compare laterV to plugin version
                    switch (pluginVersion.compareTo(laterV)) {
                        case -1 -> {
                            // BEHIND
                            CommonsAPI.tInfo(MessageKeys.Update.STATUS_BEHIND, plugin.getName());
                            doUpdate = true;
                            newVer = versionMap.get(laterV);
                            result[0] = -1;
                            statusBehind(laterV, newVer);
                        }
                        case 0 -> {
                            // UP-TO-DATE
                            CommonsAPI.tInfo(MessageKeys.Update.STATUS_UP_TO_DATE, plugin.getName());
                            result[0] = 0;
                            statusUpToDate();
                        }
                        case 1 -> {
                            // AHEAD
                            CommonsAPI.tInfo(MessageKeys.Update.STATUS_AHEAD, plugin.getName());
                            result[0] = 1;
                            statusAhead();
                        }
                    }

                    return;
                } catch (IOException e) {
                    CommonsAPI.tWarning(MessageKeys.Update.FAIL_TO_CHECK);
                    e.printStackTrace();
                }

                result[0] = 10000;
                errorChecking();
            }
        }.runTaskAsynchronously(RDSCommons.getInstance());

        lastUpdateCheck = result[0];
    }

    public void applyUpdate() {
        String newVersion = getNewestVersion();
        CommonsAPI.tInfo(MessageKeys.Update.GETTING, newVersion);
        try {
            URL download = new URL(versionURL.replace("%b%", branch).replace("%v%", newVersion));
            ReadableByteChannel rbc = Channels.newChannel(download.openStream());
            File file = new File(Path.of(plugin.getDataFolder().toURI()).getParent().toString() + "/" + plugin.getName() + "-" + newVersion + ".jar");
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            CommonsAPI.tInfo(MessageKeys.Update.DOWNLOADED);

            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File curfile = (File) getFileMethod.invoke(plugin);
            CommonsAPI.tInfo(MessageKeys.Update.APPLIED);
            curfile.deleteOnExit();
        } catch (Exception e) {
            CommonsAPI.tWarning(MessageKeys.Update.FAIL);
            e.printStackTrace();
        }
    }
    
    protected abstract void statusBehind(Version newVer, String newVerInput);
    protected abstract void statusUpToDate();
    protected abstract void statusAhead();
    protected abstract void errorChecking();

    public static void ioExceptionHandler(IOException ioexc) {
        ioexc.printStackTrace();
    }
    
    public static Version parseVersion(String versionToParse) {
        return Version.fromString(versionToParse);
    }
    
    public static HashMap<Version, String> parseMavenMetadata(URL readURL, Map<String, String> replacements) {
        HashMap<Version, String> versionMap = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(readURL.openStream()));
            String str;

            while ((str = br.readLine()) != null) {
                // Check for start of <versions> block
                if (str.contains("<versions>")) {
                    while ((str = br.readLine()) != null) {
                        // Check for end of <versions> block
                        if (!str.contains("</versions>")) {
                            if (!str.toLowerCase().contains("snapshot") || branch.equals("snapshot")) {
                                // Will continue to next version if one fails to parse
                                String line = str.replace("</version>", "").replace("<version>", "").trim();
                                try {
                                    if (replacements.containsKey(line)) {
                                        line = replacements.get(line);
                                    }
                                    versionMap.put(parseVersion(line), line);
                                } catch (IllegalArgumentException ignored) {
                                    CommonsAPI.tWarning(MessageKeys.Update.FAIL_TO_PARSE_VERSION, line);
                                }
                            }
                        } else break;
                    }
                }
            }

            br.close();
        } catch (IOException e) {
            ioExceptionHandler(e);
        }

        return versionMap;
    }
    
    private static HashMap<Version, String> parseJSON(URL readURL, Map<String, String> replacements) {
        HashMap<Version, String> versionMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(readURL.openStream()));
            String str;

            while ((str = br.readLine()) != null) {
                String line = str.replace("{", "").replace("}", "").replace(",", "").replace("\"", "").trim();
                if (!line.toLowerCase().contains("snapshot") || branch.equals("snapshot")) {
                    try {
                        if (replacements.containsKey(line)) {
                            line = replacements.get(line);
                        }
                        versionMap.put(parseVersion(line), line);
                    } catch (IllegalArgumentException ignored) {
                        CommonsAPI.tWarning(MessageKeys.Update.FAIL_TO_PARSE_VERSION, line);
                    }
                }
            }

            br.close();
        } catch (IOException e) {
            ioExceptionHandler(e);
        }

        return versionMap;
    }

    public enum Parser {
        MAVEN_METADATA("MAVEN METADATA", AutoUpdater::parseMavenMetadata),
        JSON("JSON", AutoUpdater::parseJSON),
        CUSTOM("CUSTOM", (url, replacements) -> new HashMap<>());

        private final String name;
        private BiFunction<URL, Map<String, String>, HashMap<Version, String>> parser;

        Parser(String name, BiFunction<URL, Map<String, String>, HashMap<Version, String>> parser) {
            this.name = name;
            this.parser = parser;
        }

        public String getName() {
            return name;
        }

        public BiFunction<URL, Map<String, String>, HashMap<Version, String>> getParser() {
            return parser;
        }

        public Parser setParser(BiFunction<URL, Map<String, String>, HashMap<Version, String>> parser) {
            this.parser = parser;
            return this;
        }
    }
}
