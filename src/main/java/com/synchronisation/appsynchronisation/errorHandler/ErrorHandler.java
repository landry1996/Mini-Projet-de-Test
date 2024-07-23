package com.synchronisation.appsynchronisation.errorHandler;

import com.synchronisation.appsynchronisation.enums.ErrorCode;
import com.synchronisation.appsynchronisation.error.*;
import com.synchronisation.appsynchronisation.models.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class ErrorHandler {

   @ExceptionHandler(NoteErrorException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(NoteErrorException exception){
        final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorMessage errorMessage = ErrorMessage.builder()
                .status(ErrorCode.INTERNAL_SERVE_ERROR.value)
                .httpStatus(internalServerError)
                .message(exception.getMessage())
                .timestamp(LocalDate.now())
                .build();
        return new ResponseEntity<>(errorMessage, internalServerError);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(NoteNotFoundException exception){

       final HttpStatus notFound = HttpStatus.NOT_FOUND;
       final ErrorMessage errorMessage = ErrorMessage.builder()
               .status(ErrorCode.NOT_FOUND.value)
               .httpStatus(notFound)
               .message(exception.getMessage())
               .timestamp(LocalDate.now())
               .build();
       return new ResponseEntity<>(errorMessage, notFound);
    }

    @ExceptionHandler(NoteAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(NoteAlreadyExistException exception){

       final HttpStatus alreadyReported = HttpStatus.ALREADY_REPORTED;
       final ErrorMessage errorMessage = ErrorMessage.builder()
               .status(ErrorCode.ALREADY_EXIST.value)
               .httpStatus(alreadyReported)
               .message(exception.getMessage())
               .timestamp(LocalDate.now())
               .build();
       return new ResponseEntity<>(errorMessage, alreadyReported);
    }

    @ExceptionHandler(UserErrorException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(UserErrorException exception){

       final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
       final ErrorMessage errorMessage = ErrorMessage.builder()
               .status(ErrorCode.INTERNAL_SERVE_ERROR.value)
               .httpStatus(internalServerError)
               .message(exception.getMessage())
               .timestamp(LocalDate.now())
               .build();
       return new ResponseEntity<>(errorMessage, internalServerError);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(UserNotFoundException exception){

       final HttpStatus notFound = HttpStatus.NOT_FOUND;
       final ErrorMessage errorMessage = ErrorMessage.builder()
               .status(ErrorCode.NOT_FOUND.value)
               .httpStatus(notFound)
               .message(exception.getMessage())
               .timestamp(LocalDate.now())
               .build();
       return new ResponseEntity<>(errorMessage, notFound);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(UserAlreadyExistException exception){

       final HttpStatus alreadyReported = HttpStatus.ALREADY_REPORTED;
       final ErrorMessage errorMessage = ErrorMessage.builder()
               .status(ErrorCode.ALREADY_EXIST.value)
               .httpStatus(alreadyReported)
               .message(exception.getMessage())
               .timestamp(LocalDate.now())
               .build();
       return new ResponseEntity<>(errorMessage, alreadyReported);
    }


}
