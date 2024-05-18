package net.realdarkstudios.commons.util;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.RDSCommons;

public class MessageKeys {
    public static class Api {
        public static final LocalizedMessages.Key LOADED_LOCALIZATION = key("loaded_localization", 3);
        public static final LocalizedMessages.Key REGISTERING_LOCALIZATION = key("registering_localization", 2);
        public static final LocalizedMessages.Key LOADED_PLAYERS = key("loaded_players", 1);
        public static final LocalizedMessages.Key ATTEMPTING_UPDATE = key("update", 1);
        public static final LocalizedMessages.Key NOT_ATTEMPTING_UPDATE = key("update.not_attempting", 1);
        public static final LocalizedMessages.Key UPDATE_FAILED = key("update.fail", 3);
        public static final LocalizedMessages.Key UPDATE_SUCCEEDED = key("update.succeed", 3);
        public static final LocalizedMessages.Key NEW_PLAYER_DATA = key("new_player_data", 1);

        private static LocalizedMessages.Key key(String path, int expectedParamCount) {
            return MessageKeys.keyHelper("api", path, LocalizedMessages.StyleOptions.ERROR, expectedParamCount);
        }
    }

    public static class Error {
        public static final LocalizedMessages.Key GENERIC = key("", 1);
        public static final LocalizedMessages.Key NON_CONSOLE_COMMAND = key("non_console_command", 0);
        public static final LocalizedMessages.Key INCORRECT_USAGE = key("incorrect_usage", 0);
        public static final LocalizedMessages.Key INCORRECT_ARG_COUNT = key("incorrect_arg_count", 0);
        public static final LocalizedMessages.Key INCORRECT_KEY_PARAM_COUNT = key("incorrect_key_param_count", 3);
        public static final LocalizedMessages.Key FAILED_TO_PARSE_NUMBER = key("fail_parse_number", 1);
        public static final LocalizedMessages.Key TRANSLATION_MISSING = key("translation_missing", 1);
        public static final LocalizedMessages.Key TRANSLATION_FORMAT_ARG_MISSING = key("translation_format_arg_missing", 0);
        public static final LocalizedMessages.Key MENU_ITEM_OUT_OF_BOUNDS = key("menu_item_out_of_bounds", 2);
        public static final LocalizedMessages.Key PLAYER_LIST_EMPTY = key("player_list_empty", 0);

        // PLUGIN
        public static final LocalizedMessages.Key CREATE_FILE = key("create_file", 1);
        public static final LocalizedMessages.Key UPDATE_FILE = key("update_file", 1);
        public static final LocalizedMessages.Key PARSE_UUID = key("parse_uuid", 1);


        private static LocalizedMessages.Key key(String path, int expectedParamCount) {
            return MessageKeys.keyHelper("error", path, LocalizedMessages.StyleOptions.ERROR, expectedParamCount);
        }
    }

    public static class Menu {
        public static final LocalizedMessages.Key CANCEL = key("cancel", 0, new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY).setBold(true));
        public static final LocalizedMessages.Key CLOSE = key("close", 0, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED).setBold(true));
        public static final LocalizedMessages.Key GO_BACK = key("go_back", 0, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key PAGINATION_PREVIOUS = key("pagination_previous", 0, new LocalizedMessages.StyleOptions().setUnderline(true));
        public static final LocalizedMessages.Key PAGINATION_NEXT = key("pagination_next", 0, new LocalizedMessages.StyleOptions().setUnderline(true));
        public static final LocalizedMessages.Key REFRESH = key("refresh", 0, new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
        public static final LocalizedMessages.Key PERM_CHANGED = key("perm_changed", 0, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));


        private static LocalizedMessages.Key key(String path, int expectedParamCount, LocalizedMessages.StyleOptions styleOptions) {
            return MessageKeys.keyHelper("menu", path, styleOptions, expectedParamCount);
        }
    }

    public static class Update {
        public static final LocalizedMessages.Key AVAILABLE = key("", 3, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key AVAILABE_AUTO = key("auto", 3, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key CHECKING = key("checking_for_update", 2);
        public static final LocalizedMessages.Key LATEST = key("latest", 3);
        public static final LocalizedMessages.Key FAIL_TO_CHECK = key("failcheck", 0);
        public static final LocalizedMessages.Key FAIL = key("fail", 0);
        public static final LocalizedMessages.Key GETTING = key("getting", 1);
        public static final LocalizedMessages.Key DOWNLOADED = key("downloaded", 0);
        public static final LocalizedMessages.Key APPLIED = key("applied", 0);
        public static final LocalizedMessages.Key STATUS_AHEAD = key("ahead", 1);
        public static final LocalizedMessages.Key STATUS_BEHIND = key("behind", 1);
        public static final LocalizedMessages.Key STATUS_UP_TO_DATE = key("up_to_date", 1);
        public static final LocalizedMessages.Key AUTO_DISABLED = key("disabled", 0);
        public static final LocalizedMessages.Key AUTO_DISABLED_DOWNLOAD = key("download_disabled", 0);
        public static final LocalizedMessages.Key NO_VERSIONS_AVAILABLE = key("none_available", 0);
        public static final LocalizedMessages.Key FAIL_TO_PARSE_VERSION = key("fail_to_parse_version", 1);

        private static LocalizedMessages.Key key(String path, int expectedParamCount) {
            return key(path, expectedParamCount, LocalizedMessages.StyleOptions.NONE);
        }

        private static LocalizedMessages.Key key(String path, int expectedParamCount, LocalizedMessages.StyleOptions styleOptions) {
            return MessageKeys.keyHelper( "update", path, styleOptions, expectedParamCount);
        }
    }

    // Makes some common keys more accessible
    public static final LocalizedMessages.Key ERROR = Error.GENERIC;
    public static final LocalizedMessages.Key INCORRECT_USAGE = Error.INCORRECT_USAGE;
    public static final LocalizedMessages.Key TRANSLATION_MISSING = Error.TRANSLATION_MISSING;
    public static final LocalizedMessages.Key TRANSLATION_FORMAT_ARG_MISSING = Error.TRANSLATION_FORMAT_ARG_MISSING;

    private static LocalizedMessages.Key key(String path, int expectedParamCount) {
        return key(path, LocalizedMessages.StyleOptions.NONE, expectedParamCount);
    }

    private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions, int expectedParamCount) {
        return new LocalizedMessages.Key(RDSCommons.getAPI().getAPILocalization(), path, styleOptions, expectedParamCount);
    }

    private static LocalizedMessages.Key keyHelper(String section, String path, LocalizedMessages.StyleOptions styleOptions, int expectedParamCount) {
        return key(path.isEmpty() ? section : section + "." + path, styleOptions, expectedParamCount);
    }

    public static void init() {
    }
}
