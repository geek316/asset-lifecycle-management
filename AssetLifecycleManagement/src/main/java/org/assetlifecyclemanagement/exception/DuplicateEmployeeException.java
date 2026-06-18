package org.assetlifecyclemanagement.exception;

public class DuplicateEmployeeException extends RuntimeException{

    public DuplicateEmployeeException(Long empId) {
        super("Employee already exists with empId: " + empId);
    }

    public DuplicateEmployeeException(String field, String value) {
        super("Employee already exists with " + field + ": " + value);
    }

    public DuplicateEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }

}
