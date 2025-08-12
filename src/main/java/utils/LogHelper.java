package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LogHelper {

    private static Logger getLogger() {
        // Gets the caller class
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
    }

    private LogHelper() {
    }

    public static void info(String message) {
        getLogger().info(message);
    }

    public static void warn(String message) {
        getLogger().warn(message);
    }

    public static void error(String message) {
        getLogger().error(message);
    }

    public static void debug(String message) {
        getLogger().debug(message);
    }

    public static void trace(String message) {
        getLogger().trace(message);
    }

    public static void fatal(String message) {
        getLogger().fatal(message);
    }

    // Optional: log exceptions with messages
    public static void error(String message, Throwable throwable) {
        getLogger().error(message, throwable);
    }

    public static void warn(String message, Throwable throwable) {
        getLogger().warn(message, throwable);
    }
}
