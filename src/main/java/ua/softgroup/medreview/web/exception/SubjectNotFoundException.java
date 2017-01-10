package ua.softgroup.medreview.web.exception;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class SubjectNotFoundException extends MedReviewException {

    private static final String SUBJECT_NOT_FOUND_FORMAT = "Subject \"%s\" not found.";

    public SubjectNotFoundException(String message) {
        super(String.format(SUBJECT_NOT_FOUND_FORMAT, message));
    }
}
