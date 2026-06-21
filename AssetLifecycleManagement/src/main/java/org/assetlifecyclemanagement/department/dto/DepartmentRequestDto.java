package org.assetlifecyclemanagement.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.assetlifecyclemanagement.enums.Department;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDto {

    @NotNull(message = "Department code is required")
    private Department deptCode;  // Enum type - automatically validated by Spring

    @NotBlank(message = "Department name is required")
    private String deptName;

}
