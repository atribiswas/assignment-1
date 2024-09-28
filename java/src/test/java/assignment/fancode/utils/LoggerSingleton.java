package assignment.fancode.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerSingleton {

    public static final Logger logger = LogManager.getLogger(LoggerSingleton.class);

    static {
        logger.info("Logger has been initialized.");
    }
}