package co.leapwise.expressionevaluator.dto;

import java.util.HashMap;
import java.util.Map;

public class ErrorDetailResponse {

    private String errorCode;

    private String message;

    private Map<String, String> errors;

    public ErrorDetailResponse() {
        // default constructor to deserialize from JSON String
    }

    public ErrorDetailResponse(final String errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String errorMessage) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(fieldName, errorMessage);
    }

}
