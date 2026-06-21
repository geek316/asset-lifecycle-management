package org.assetlifecyclemanagement.exception;

import org.assetlifecyclemanagement.enums.Department;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(Department deptCode) { super("Department not found with deptCode: " + deptCode);}

    public DepartmentNotFoundException(String message, Throwable cause) { super(message, cause);}


}
