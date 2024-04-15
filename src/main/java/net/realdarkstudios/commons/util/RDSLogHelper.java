package net.realdarkstudios.commons.util;

import java.util.logging.Logger;

/**
A class that has some helper methods for integrating with the {@link LocalizedMessages} system
 */
public class RDSLogHelper {
    private final Logger logger;

    /**
     * Creates a new {@link RDSLogHelper}
     * @param logger The {@link Logger} to inherit from.
     */
    RDSLogHelper(Logger logger) {
        this.logger = logger;
    }

    /**
     * Gets the stored logger
     * @return The {@link Logger}
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Logs all the messages at the INFO level
     * @param messages The messages to log
     * @since 1.0.0.0
     */
    public void info(String... messages) {
        for (String msg : messages) {
            logger.info(msg);
        }
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the INFO level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
     * @since 1.0.0.0
     */
    public void tInfo(LocalizedMessages.Key key, Object... formatArgs) {
        info(key.console(formatArgs));
    }

    public void tInfoPrefix(LocalizedMessages.Key key, Object... formatArgs) {
        info(key.consoleWithPrefix(true, formatArgs));
    }

    /**
     * Logs all the messages at the WARN level
     * @param messages The messages to log
     * @since 1.0.0.0
     */
    public void warning(String... messages) {
        for (String msg : messages) {
            logger.warning(msg);
        }
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the WARNING level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
     * @since 1.0.0.0
     */
    public void tWarning(LocalizedMessages.Key key, Object... formatArgs) {
        warning(key.console(formatArgs));
    }

    public void tWarningPrefix(LocalizedMessages.Key key, Object... formatArgs) {
        warning(key.consoleWithPrefix(true, formatArgs));
    }
}
