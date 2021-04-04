package ru.imatveev.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
public class OopsException extends RuntimeException {
    public OopsException() {
    }

    public OopsException(String message) {
        super(message);
    }

    public OopsException(String message, Throwable cause) {
        super(message, cause);
    }

    public static OopsException init() {
        return new OopsException("Ooooooooooooooooops");
    }
}
