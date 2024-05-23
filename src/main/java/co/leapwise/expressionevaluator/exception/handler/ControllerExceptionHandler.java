package co.leapwise.expressionevaluator.exception.handler;

import co.leapwise.expressionevaluator.dto.ErrorDetailResponse;
import co.leapwise.expressionevaluator.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    public static final String INVALID_REQUEST_CODE = "400001";

    public static final String RESOURCE_NOT_FOUND_CODE = "404001";


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetailResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final ErrorDetailResponse errorDetail = new ErrorDetailResponse(INVALID_REQUEST_CODE, "Invalid request data");

        if (!CollectionUtils.isEmpty(ex.getFieldErrors())) {
            for (FieldError fieldError : ex.getFieldErrors()) {
                errorDetail.addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        return errorDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetailResponse handleResourceNotFoundException(final ResourceNotFoundException ex) {
        return new ErrorDetailResponse(RESOURCE_NOT_FOUND_CODE, "Resource not found");
    }
}
