package tourGuide.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ExceptionDetails {
    /**
     * the time when the exception is thrown
     */
    private final LocalDateTime timestamp;
    /**
     * the exception's message
     */
    private final String message;

    /**
     * HttpStatus of the response
     */
    private final HttpStatus httpStatus;

    /**
     * exception's details
     */
    private final String details;

    /**
     * all fields class constructor
     *
     * @param times
     * @param message
     * @param httpStatus
     * @param details
     */
    public ExceptionDetails(LocalDateTime times, String message,
            HttpStatus httpStatus, String details) {

        this.timestamp = times;
        this.message = message;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    /**
     * Getter of the exception's timestamp.
     *
     * @return timestamp
     */
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * Getter of the exception's message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter of the exception's httpStatus.
     *
     * @return httpStatus
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Getter of the exception's details.
     *
     * @return details
     */
    public String getDetails() {
        return details;
    }

}
