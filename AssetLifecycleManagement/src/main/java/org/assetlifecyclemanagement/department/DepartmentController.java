package org.assetlifecyclemanagement.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.assetlifecyclemanagement.department.dto.DepartmentRequestDto;
import org.assetlifecyclemanagement.department.dto.DepartmentResponseDto;
import org.assetlifecyclemanagement.enums.Department;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "APIs for managing department records")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Get all department",
            description = "Retrieves a list of all employees in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "department found successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "department not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> findAllDepartments() {
        List<DepartmentResponseDto> response = departmentService.findAllDepartments();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new department",
            description = "Creates a new department record in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "department created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "409", description = "department code already exists")
    })
    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto response = departmentService.createDepartment(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing department",
            description = "Updates department information using department code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "department updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "department not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error", content = @Content)
    })
    @PutMapping
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto response = departmentService.updateDepartment(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a department",
            description = "Deletes a department record from the system using department code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "department deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "department not found", content = @Content)
    })
    @DeleteMapping("/{deptCode}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Department deptCode) {
        departmentService.deleteDepartment(deptCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
