package ua.softgroup.medreview.web.exception;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class TreatmentNotFoundException extends MedReviewException {

    private static final String TREATMENT_NOT_FOUND_FORMAT = "Treatment \"%s\" not found.";

    public TreatmentNotFoundException(String message) {
        super(String.format(TREATMENT_NOT_FOUND_FORMAT, message));
    }
}
