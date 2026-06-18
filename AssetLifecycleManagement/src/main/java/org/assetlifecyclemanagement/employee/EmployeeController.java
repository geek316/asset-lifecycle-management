package org.assetlifecyclemanagement.employee;

import org.assetlifecyclemanagement.config.MultiLevelCacheManager;
import org.assetlifecyclemanagement.employee.dto.EmployeeCreateRequestDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeResponseDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employee records")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final MultiLevelCacheManager multiLevelCacheManager;

    @Operation(summary = "Get employee by employee code", description = "Retrieves detailed information of a specific employee using their unique employee code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeResponseDTO response = employeeService.getEmployee(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees retrieved successfully", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> response = employeeService.getAllEmployees();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Search employees with filters", description = "Retrieves employees based on optional department and status filters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees found successfully", content = @Content)
    })
    @GetMapping("/filter")
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
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }


    @Hidden
    @GetMapping("/cache/stats")
    public ResponseEntity<String> getCacheStats() {
        Cache cache = multiLevelCacheManager.getCache("employees");

        if (cache instanceof ConcurrentMapCache concurrentMapCache) {
            Map<Object, Object> nativeCache = concurrentMapCache.getNativeCache();
            StringBuilder sb = new StringBuilder("Cache contents:\n");
            nativeCache.forEach((key, value) -> sb.append(key).append(" = ").append(value).append("\n"));
            return ResponseEntity.ok(sb.toString());
        }

        return ResponseEntity.ok("No cache stats available or unsupported cache type");
    }

    @Hidden
    @DeleteMapping("/cache/clear")
    public ResponseEntity<String> clearCache() {
        // Clear all caches
        multiLevelCacheManager.getCacheNames().forEach(name -> {
            Cache cache = multiLevelCacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
                log.info("Cleared cache: {}", name);
            }
        });
        return ResponseEntity.ok("All caches cleared successfully");
    }

    @Hidden
    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<>("Health Ok!", HttpStatus.OK);
    }

}
