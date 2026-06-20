package org.assetlifecyclemanagement.employee;

import org.assetlifecyclemanagement.employee.dto.EmployeeCreateRequestDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeResponseDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeUpdateRequestDTO;
import org.assetlifecyclemanagement.utilities.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    PaginatedResponse<EmployeeResponseDTO> getAllEmployees(Pageable pageable);

    EmployeeResponseDTO getEmployee(Long employeeId);

    List<EmployeeResponseDTO> getEmployeesByFilters(String department, String status);

    EmployeeResponseDTO createEmployee(EmployeeCreateRequestDTO dto);

    EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeUpdateRequestDTO dto);

    void deleteEmployee(Long empId);

}
