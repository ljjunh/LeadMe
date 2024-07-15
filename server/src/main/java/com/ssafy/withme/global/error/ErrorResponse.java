package com.ssafy.withme.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {

    private String errorStatus;
    private Integer errorCode;
    private String errorMessage;

    public static ErrorResponse of(String errorStatus, Integer errorCode, String errorMessage) {

        return ErrorResponse.builder()
                .errorStatus(errorStatus)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static ErrorResponse of(String errorStatus, Integer errorCode, BindingResult bindingResult){
        return ErrorResponse.builder()
                .errorStatus(errorStatus)
                .errorCode(errorCode)
                .errorMessage(createErrorMessage(bindingResult))
                .build();
    }

    public static String createErrorMessage(BindingResult bindingResult){

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError fieldError: fieldErrors){
            if (!isFirst)
                sb.append(", ");
            else isFirst = false;

            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("]");
            sb.append(fieldError.getDefaultMessage());
        }

        return sb.toString();
    }
}
