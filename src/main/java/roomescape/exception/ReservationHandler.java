package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReservationHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
