package org.assetlifecyclemanagement.department.dto;

import lombok.*;
import org.assetlifecyclemanagement.enums.Department;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDto {

    private Long deptId;
    private Department deptCode;
    private String deptName;
}
