package net.realdarkstudios.commons.util;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.RDSCommons;

public class MessageKeys {
    public static class Api {
        public static final LocalizedMessages.Key LOADED_LOCALIZATION = key("loaded_localization");
        public static final LocalizedMessages.Key REGISTERING_LOCALIZATION = key("registering_localization");

        private static LocalizedMessages.Key key(String path) {
            return key(path, LocalizedMessages.StyleOptions.ERROR);
        }

        private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions options) {
            return MessageKeys.keyHelper("api", path, options);
        }
    }
    public static class Error {
        public static final LocalizedMessages.Key GENERIC = key("");
        public static final LocalizedMessages.Key NON_CONSOLE_COMMAND = key("non_console_command");
        public static final LocalizedMessages.Key INCORRECT_USAGE = key("incorrect_usage");
        public static final LocalizedMessages.Key INCORRECT_ARG_COUNT = key("incorrect_arg_count");
        public static final LocalizedMessages.Key FAILED_TO_PARSE_NUMBER = key("fail_parse_number");
        public static final LocalizedMessages.Key TRANSLATION_MISSING = key("translation_missing");
        public static final LocalizedMessages.Key TRANSLATION_FORMAT_ARG_MISSING = key("translation_format_arg_missing");
        public static final LocalizedMessages.Key MENU_ITEM_OUT_OF_BOUNDS = key("menu_item_out_of_bounds");


        private static LocalizedMessages.Key key(String path) {
            return key(path, LocalizedMessages.StyleOptions.ERROR);
        }

        private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions options) {
            return MessageKeys.keyHelper("error", path, options);
        }
    }

    public static class Menu {
        public static final LocalizedMessages.Key CANCEL = key("cancel", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY).setBold(true));
        public static final LocalizedMessages.Key CLOSE = key("close", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED).setBold(true));
        public static final LocalizedMessages.Key GO_BACK = key("go_back", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key PAGINATION_PREVIOUS = key("pagination_previous", new LocalizedMessages.StyleOptions().setUnderline(true));
        public static final LocalizedMessages.Key PAGINATION_NEXT = key("pagination_next", new LocalizedMessages.StyleOptions().setUnderline(true));
        public static final LocalizedMessages.Key REFRESH = key("refresh", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
        public static final LocalizedMessages.Key PERM_CHANGED = key("perm_changed", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));

        private static LocalizedMessages.Key key(String path) {
            return key(path, LocalizedMessages.StyleOptions.NONE);
        }

        private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
            return MessageKeys.keyHelper("menu", path, styleOptions);
        }
    }

    public static class Update {
        public static final LocalizedMessages.Key AVAILABLE = key("", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key AVAILABE_AUTO = key("auto", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key CHECKING = key("checking_for_update");
        public static final LocalizedMessages.Key LATEST = key("latest");
        public static final LocalizedMessages.Key FAIL_TO_CHECK = key("failcheck");
        public static final LocalizedMessages.Key FAIL = key("fail");
        public static final LocalizedMessages.Key GETTING = key("getting");
        public static final LocalizedMessages.Key DOWNLOADED = key("downloaded");
        public static final LocalizedMessages.Key APPLIED = key("applied");
        public static final LocalizedMessages.Key STATUS_AHEAD = key("ahead");
        public static final LocalizedMessages.Key STATUS_BEHIND = key("behind");
        public static final LocalizedMessages.Key STATUS_UP_TO_DATE = key("up_to_date");
        public static final LocalizedMessages.Key AUTO_DISABLED = key("disabled");
        public static final LocalizedMessages.Key AUTO_DISABLED_DOWNLOAD = key("download_disabled");
        public static final LocalizedMessages.Key NO_VERSIONS_AVAILABLE = key("none_available");
        public static final LocalizedMessages.Key FAIL_TO_PARSE_VERSION = key("fail_to_parse_version");

        private static LocalizedMessages.Key key(String path) {
            return key(path, LocalizedMessages.StyleOptions.NONE);
        }

        private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
            return MessageKeys.keyHelper( "update", path, styleOptions);
        }
    }

    // Makes some common keys more accessible
    public static final LocalizedMessages.Key ERROR = Error.GENERIC;
    public static final LocalizedMessages.Key INCORRECT_USAGE = Error.INCORRECT_USAGE;
    public static final LocalizedMessages.Key TRANSLATION_MISSING = Error.TRANSLATION_MISSING;
    public static final LocalizedMessages.Key TRANSLATION_FORMAT_ARG_MISSING = Error.TRANSLATION_FORMAT_ARG_MISSING;

    private static LocalizedMessages.Key key(String path) {
        return key(path, LocalizedMessages.StyleOptions.NONE);
    }

    private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
        return new LocalizedMessages.Key(RDSCommons.getAPI().getAPILocalization(), path, styleOptions);
    }

    private static LocalizedMessages.Key keyHelper(String section, String path, LocalizedMessages.StyleOptions styleOptions) {
        return key(path.isEmpty() ? section : section + "." + path, styleOptions);
    }

    public static void init() {
    }
}
