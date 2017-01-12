package ua.softgroup.medreview.web.exception;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class UserNotFoundException extends MedReviewException {
    private static final String USER_NOT_FOUND_FORMAT = "User \"%s\" not found.";

    public UserNotFoundException(String message) {
        super(String.format(USER_NOT_FOUND_FORMAT, message));
    }
}
