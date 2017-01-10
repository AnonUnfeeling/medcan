package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.softgroup.medreview.web.exception.MedReviewException;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedReviewException.class)
    public ResponseEntity handleException(MedReviewException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
