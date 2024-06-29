package com.hisham.HomeCentre.exceptions;

import com.hisham.HomeCentre.exceptions.CustomExceptions.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        // Extract validation error messages
        System.out.println(exception.getBindingResult().getAllErrors());

        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception, WebRequest request) {
        String errorMessage = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                exception.getName(), exception.getValue(), exception.getRequiredType().getSimpleName());

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = String.format("Missing required parameter: %s", exception.getParameterName());

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = String.format("Missing path variable: %s", exception.getVariableName());

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = String.format("Media type '%s' is not supported. Supported media types are: %s",
                exception.getContentType(), exception.getSupportedMediaTypes());

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Binding error");

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, WebRequest request) {
        String errorMessage = String.format("Request method '%s' not supported. Supported methods are: %s", exception.getMethod(), exception.getSupportedHttpMethods());

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        String errorMessage = "You do not have permission to perform this action";

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDTO> handleBadRequestException(BadRequestException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionDTO> handleConflictException(ConflictException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionDTO> handleUnauthorizedException(UnauthorizedException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDTO> handleForbiddenException(ForbiddenException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionDTO> handleServiceUnavailableException(ServiceUnavailableException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleCartNotFoundException(CartNotFoundException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CloudinaryException.class)
    public ResponseEntity<ExceptionDTO> handleCloudinaryException(CloudinaryException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ExceptionDTO> handleImageUploadException(ImageUploadException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ExceptionDTO> handlePaymentException(PaymentException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ExceptionDTO> handleEmailException(EmailException exception, WebRequest request) {
        String errorMessage = exception.getMessage();

        ExceptionDTO errorDetails = new ExceptionDTO(new Date().toInstant(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception exception, WebRequest request){

        ExceptionDTO exceptionDTO = new ExceptionDTO(
                new Date().toInstant(),
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
