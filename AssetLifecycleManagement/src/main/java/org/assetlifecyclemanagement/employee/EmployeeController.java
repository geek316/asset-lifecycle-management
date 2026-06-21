package org.assetlifecyclemanagement.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assetlifecyclemanagement.employee.dto.EmployeeCreateRequestDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeResponseDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeUpdateRequestDTO;
import org.assetlifecyclemanagement.utilities.PaginatedResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employee records")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Get employee by employee code",
            description = "Retrieves detailed information of a specific employee using their unique employee code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @GetMapping("/{employeeId}")
//    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeResponseDTO response = employeeService.getEmployee(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees retrieved successfully", content = @Content)
    })
    @GetMapping
//    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponse<EmployeeResponseDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PaginatedResponse<EmployeeResponseDTO> response = employeeService.getAllEmployees(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Search employees with filters", description = "Retrieves employees based on optional department and status filters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees found successfully", content = @Content)
    })
    @GetMapping("/filter")
//    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByFilters(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status) {
        List<EmployeeResponseDTO> response = employeeService.getEmployeesByFilters(department, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new employee", description = "Creates a new employee record in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "409", description = "Employee code or email already exists")
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeCreateRequestDTO dto) {
        EmployeeResponseDTO response = employeeService.createEmployee(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing employee", description = "Updates employee information using employee code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error", content = @Content)
    })
    @PutMapping("/{employeeId}")
//    @PreAuthorize("hasAuthority('EMPLOYEE_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeUpdateRequestDTO dto) {
        EmployeeResponseDTO response = employeeService.updateEmployee(employeeId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee record from the system using employee code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @DeleteMapping("/{employeeId}")
//    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
