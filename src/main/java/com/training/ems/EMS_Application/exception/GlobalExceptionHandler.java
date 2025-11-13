package com.training.ems.EMS_Application.exception;

import com.training.ems.EMS_Application.dtos.response.ErrorListResponse;
import com.training.ems.EMS_Application.dtos.response.FieldError;
import com.training.ems.EMS_Application.dtos.response.StatusResponse;
import com.training.ems.EMS_Application.dtos.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// we need to use the respose entity in this global exception handler else our response will be ignored and spring throws its default error...
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCompanyNotFoundException(CompanyNotFoundException e) {
        StatusResponse statusResponse = new StatusResponse(404, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusResponse(statusResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }



    @ExceptionHandler(ConflictWhileDeletingCompany.class)
    public ResponseEntity<ErrorResponse> handleConflictWhileDeletingCompany(ConflictWhileDeletingCompany e) {
        StatusResponse statusResponse = new StatusResponse(409, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusResponse(statusResponse);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        StatusResponse statusResponse = new StatusResponse(404, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusResponse(statusResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorListResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldError> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        StatusResponse status = new StatusResponse(400, "Validation errors occurred");


        ErrorListResponse response = new ErrorListResponse();
        response.setFieldErrors(errorDetails);
        response.setStatusResponse(status);

        return ResponseEntity.badRequest().body(response);
    }


}
