package org.assetlifecyclemanagement.exception;

public class DuplicateDepartmentException extends RuntimeException {

    public DuplicateDepartmentException(String message) {
        super(message);
    }

    public DuplicateDepartmentException(String field, String value) {
        super("Department already exists with " + field + ": " + value);
    }

}
