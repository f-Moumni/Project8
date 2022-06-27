package tourGuide.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
@ControllerAdvice
public class TourGuideExceptionHandler {


    /**
     * handleDataNoteFoundException the handle method of DataNotFoundException
     *
     * @param e
     *            DataNotFoundException
     * @param request
     *            WebRequest
     * @return
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNoteFoundException(
            DataNotFoundException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.NOT_FOUND,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);

    }

    /**
     * handleAlreadyExistsException the handle method of AlreadyExistsException
     *
     * @param e
     *            AlreadyExistsException
     * @param request
     *            WebRequest
     * @return
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(
            AlreadyExistsException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.ALREADY_REPORTED,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.ALREADY_REPORTED);

    }
}
