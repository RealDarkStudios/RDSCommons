package net.realdarkstudios.commons.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.commons.CommonsAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocalizedMessages {
    /**
     * Sends the translated key with the format arguments
     * @param sender The {@link CommandSender} to send the message to
     * @param key The {@link Key} to send
     * @param formatArgs The format arguments
     * @since 1.0.0.0
     */
    public static void send(CommandSender sender, Key key, Object... formatArgs) {
         sendMessage(sender, key, key.styleOptions(), false, formatArgs);
    }

    public static void sendWithPrefix(CommandSender sender, Key key, Object... formatArgs) {
        sendMessage(sender, key, key.styleOptions(), true, formatArgs);
    }

    public static void sendMessages(CommandSender sender, String... messages) {
        sender.sendMessage(messages);
    }

    public static void sendComponents(CommandSender sender, TextComponent... textComponents) {
        if (!(sender instanceof Player plr)) CommonsAPI.info("Can't send component to non-player!");
        else plr.spigot().sendMessage(textComponents);
    }

    /**
     * Sends the translated key with the format arguments
     * @param sender The {@link CommandSender} to send the message to
     * @param key The {@link Key} to send
     * @param styleOverride The {@link StyleOptions} that will be applied instead of the key's
     * @param formatArgs The format arguments
     * @since 1.0.0.0
     */
    public static void send(CommandSender sender, Key key, StyleOptions styleOverride, Object... formatArgs) {
        sendMessage(sender, key, styleOverride,false, formatArgs);
    }

    /**
     * Sends the translated key with the format arguments
     * @param sender The {@link CommandSender} to send the message to
     * @param key The {@link Key} to send
     * @param styleOverride The {@link StyleOptions} that will be applied instead of the key's
     * @param formatArgs The format arguments
     * @since 1.0.0.0
     */
    public static void sendWithPrefix(CommandSender sender, Key key, StyleOptions styleOverride, Object... formatArgs) {
        sendMessage(sender, key, styleOverride,true, formatArgs);
    }

    private static void sendMessage(CommandSender sender, Key key, StyleOptions styleOverride, boolean withPrefix, Object... formatArgs) {
        if (sender instanceof Player plr) plr.spigot().sendMessage(styleOverride.applyStyle(translation(key, MessageKeys.ERROR, withPrefix, formatArgs)));
        else sender.spigot().sendMessage(styleOverride.applyStyle(new TextComponent(translationC(key, MessageKeys.ERROR, withPrefix, formatArgs))));
    }

    /**
     * Returns the translated message with style options
     * @param primaryKey The primary {@link Key}
     * @param backupKey The backup {@link Key} (used if the primary one doesn't exist)
     * @param formatArgs The format arguments
     * @return The {@link TextComponent}
     * @since 1.0.0.0
     */
    public static TextComponent translation(@NotNull LocalizedMessages.Key primaryKey, @NotNull LocalizedMessages.Key backupKey, boolean withPrefix, Object... formatArgs) {
        return primaryKey.localizationHasPath() ? primaryKey.translateComponentWithPrefix(withPrefix, formatArgs) :
                backupKey.equals(MessageKeys.ERROR) ?
                    backupKey.translateComponentWithPrefix(withPrefix, primaryKey.getPath())
                : backupKey.equals(MessageKeys.TRANSLATION_MISSING) ?
                    backupKey.translateComponentWithPrefix(withPrefix, formatArgs) :
                    translation(backupKey, MessageKeys.TRANSLATION_MISSING, withPrefix, formatArgs);
    }

    public static String translationC(@NotNull LocalizedMessages.Key primaryKey, @NotNull LocalizedMessages.Key backupKey, boolean withPrefix, Object... formatArgs) {
        return primaryKey.localizationHasPath() ? primaryKey.consoleWithPrefix(withPrefix, formatArgs) :
                backupKey.equals(MessageKeys.ERROR) ?
                    backupKey.consoleWithPrefix(withPrefix, primaryKey.getPath())
                : backupKey.equals(MessageKeys.TRANSLATION_MISSING) ?
                    backupKey.consoleWithPrefix(withPrefix, backupKey.equals(MessageKeys.ERROR) ? new Object[]{primaryKey.getPath()} : formatArgs) :
                    translationC(backupKey, MessageKeys.TRANSLATION_MISSING, withPrefix, formatArgs);
    }


    /**
     * Creates a new Localization Message Key
     * @param localization The {@link Localization} this key will use
     * @param path The path of the key (such as "plugin.update.fail")
     * @param styleOptions The {@link StyleOptions} this key will use
     * @param expectedParamCount The amount of expected parameters.
     *       For example, for the string {@code "Minecraft Version %s"}, it would be 1, but for {@code "Minecraft Version %d.%d.%d} it would be 3.
     * @since 1.0.0.0
     */
    public record Key(Localization localization, String path, StyleOptions styleOptions, int expectedParamCount) {
        /**
         * Returns the localization that this key will use
         * @return The {@link Localization} of this key
         */
        public Localization getLocalization() {
            return localization;
        }

        public int getExpectedParamCount() {
            return expectedParamCount;
        }

        /**
         * Returns the path inside the localization this key uses
         * @return The path of this key
         */
        public String getPath() {
            return path;
        }

        /**
         * Returns this key's style options
         * @return The {@link StyleOptions} of this key
         */
        public StyleOptions getStyleOptions() {
            return styleOptions;
        }

        /**
         * Gets the raw message (no formatting)
         * @return The raw message
         * @since 1.0.0.0
         */
        public String getRawMessage() {
            return localization.getTranslation(path);
        }

        /**
         * Gets the message
         * @param formatArgs The format arguments
         * @return The message
         * @since 1.0.0.0
         */
        public String getMessage(Object... formatArgs) {
            try {
                return String.format(getRawMessage(), formatArgs);
            } catch (Exception e) {
                return getRawMessage();
            }
        }

        public TextComponent getPrefix() {
            return new TextComponent(localization.getPrefix().endsWith(" ") ? localization.getPrefix() : localization.getPrefix() + " ");
        }

        /**
         * Translates this {@link Key} into a TextComponent
         * @param formatArgs The format arguments
         * @return The translated {@link TextComponent}
         * @since 1.0.0.0
         */
        public TextComponent translateComponent(Object... formatArgs) {
            return translateComponentWithOtherStyle(styleOptions, formatArgs);
        }

        /**
         * Translates this {@link Key} into a TextComponent
         * @param formatArgs The format arguments
         * @return The translated {@link TextComponent}
         * @since 1.0.0.0
         */
        public TextComponent translateComponentWithPrefix(boolean withPrefix, Object... formatArgs) {
            if (withPrefix) {
                TextComponent component = getPrefix();
                component.addExtra(translateComponent(formatArgs));
                return component;
            } else return translateComponent(formatArgs);
        }

        /**
         * Translates this {@link Key} into a string
         * @param formatArgs The format arguments
         * @return The translated string
         * @since 1.0.0.0
         */
        public String translate(Object... formatArgs) {
            return translateWithOtherStyle(styleOptions, formatArgs);
        }

        /**
         * Translates this {@link Key} into a TextComponent with different {@link StyleOptions}
         * @param styleOptions The overriding {@link StyleOptions}
         * @param formatArgs The format arguments
         * @return The translated {@link TextComponent}
         * @since 1.0.0.0
         */
        public TextComponent translateComponentWithOtherStyle(StyleOptions styleOptions, Object... formatArgs) {
            if (formatArgs.length != expectedParamCount) {
                CommonsAPI.tWarning(MessageKeys.Error.INCORRECT_KEY_PARAM_COUNT, path, formatArgs.length, expectedParamCount);
                if (formatArgs.length < expectedParamCount) {
                    return styleOptions.applyStyle(new TextComponent(path));
                }
            }
            return styleOptions.applyStyle(new TextComponent(getMessage(formatArgs)));
        }

        /**
         * Translates this {@link Key} into a string with different {@link StyleOptions}
         * @param styleOptions The overriding {@link StyleOptions}
         * @param formatArgs The format arguments
         * @return The translated string
         * @since 1.0.0.0
         */
        public String translateWithOtherStyle(StyleOptions styleOptions, Object... formatArgs) {
            return styleOptions.applyStyle(new TextComponent(getMessage(formatArgs))).toLegacyText();
        }

        /**
         * A dedicated version of {@link Key#translateComponent(Object...)} that removes all formatting codes from the output
         * @param formatArgs The format arguments
         * @return The translated string
         * @since 1.0.0.0
         */
        public String console(Object... formatArgs) {
            return translateComponent(formatArgs).toLegacyText().replaceAll("(§[0-9a-fk-or])", "");
        }

        public String consoleWithPrefix(boolean withPrefix, Object... formatArgs) {
            if (withPrefix) {
                TextComponent component = getPrefix();
                component.addExtra(translateComponent(formatArgs));
                return component.toLegacyText().replaceAll("(§[0-9a-fk-or])", "");
            } else return translateComponent(formatArgs).toLegacyText().replaceAll("(§[0-9a-fk-or])", "");
        }

        /**
         * Checks if the {@link Localization} has this path in it
         * @return {@code true} if the localization has the path, {@code false} otherwise
         * @since 1.0.0.0
         */
        public boolean localizationHasPath() {
            return localization.hasTranslation(path);
        }
    }

    public static class StyleOptions {
        public static final StyleOptions NONE = new StyleOptions().setColor(ChatColor.RESET);
        public static final StyleOptions ERROR = new StyleOptions().setColor(ChatColor.RED);
        public static final StyleOptions SUCCESS = new StyleOptions().setColor(ChatColor.GREEN);

        private ClickEvent clickEvent;
        private HoverEvent hoverEvent;
        private String font;
        private ChatColor color;
        private boolean bold, italic, underline, strikethrough, obfuscate;

        public StyleOptions() {
            // Resets the italicizing for renamed items (especially for gui menus)
            this.color = ChatColor.RESET;
        }

        public StyleOptions setBold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public StyleOptions setItalic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public StyleOptions setUnderline(boolean underline) {
            this.underline = underline;
            return this;
        }

        public StyleOptions setStrikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public StyleOptions setObfuscate(boolean obfuscate) {
            this.obfuscate = obfuscate;
            return this;
        }

        public StyleOptions setColor(ChatColor color) {
            this.color = color;
            return this;
        }

        public StyleOptions setFont(String font) {
            this.font = font;
            return this;
        }

        public StyleOptions setClickEvent(ClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        public StyleOptions setClickEvent(ClickEvent.Action action, String value) {
            return setClickEvent(new ClickEvent(action, value));
        }

        public StyleOptions setHoverEvent(HoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public TextComponent applyStyle(TextComponent component) {
            component.setColor(this.color);
            component.setBold(this.bold);
            component.setItalic(this.italic);
            component.setUnderlined(this.underline);
            component.setStrikethrough(this.strikethrough);
            component.setObfuscated(this.obfuscate);
            component.setFont(this.font);
            component.setClickEvent(this.clickEvent);
            component.setHoverEvent(this.hoverEvent);
            return component;
        }
    }
}
