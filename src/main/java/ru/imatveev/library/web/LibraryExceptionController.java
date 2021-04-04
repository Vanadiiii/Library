package ru.imatveev.library.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.imatveev.library.exception.OopsException;

@ControllerAdvice
public class LibraryExceptionController {
    @ExceptionHandler(value = OopsException.class)
    public ResponseEntity<Object> catchException() {
        return new ResponseEntity<>("Something going wrong...", HttpStatus.I_AM_A_TEAPOT);
    }
}
