package es.jotero.skybags.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkybagsLogger {

    private Logger logger;

    public SkybagsLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    public void info(String msg) {
        logger.info(TextColors.get(TextColors.Colors.YELLOW, 0 , msg));
    }

    public void error(String msg) {
        logger.error(TextColors.get(TextColors.Colors.RED, 0 , msg));
    }

    public void error(String msg, Throwable throwable) {
        logger.error(TextColors.get(TextColors.Colors.RED, 0 , msg), throwable);
    }
}
