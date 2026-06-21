package org.assetlifecyclemanagement.department;

import org.assetlifecyclemanagement.department.dto.DepartmentRequestDto;
import org.assetlifecyclemanagement.department.dto.DepartmentResponseDto;
import org.assetlifecyclemanagement.enums.Department;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponseDto> findAllDepartments();

    DepartmentResponseDto createDepartment(DepartmentRequestDto dto);

    DepartmentResponseDto updateDepartment(DepartmentRequestDto dto);

    void deleteDepartment(Department deptCode);

}
