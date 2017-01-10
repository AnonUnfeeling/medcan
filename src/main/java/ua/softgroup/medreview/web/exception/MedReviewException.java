package ua.softgroup.medreview.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class MedReviewException extends RuntimeException {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MedReviewException(String message) {
        super(message);
        logger.debug(message);
    }
}
