package com.holidays.exception;

import com.holidays.model.HolidayErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HolidayControllerAdvisor {

    @ExceptionHandler(HolidayException.class)
    public ResponseEntity<HolidayErrorResponse> handleHolidayExceptioException(HolidayException ex) {
        HolidayErrorResponse error = new HolidayErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(error, resolveHttpStatus(error.errCode()));
    }

    private HttpStatus resolveHttpStatus(int errCode){
        if(errCode == 0)
            return HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatus status =  HttpStatus.resolve(errCode);
        return status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
