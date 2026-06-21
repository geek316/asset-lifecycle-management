package org.assetlifecyclemanagement.employee;

import org.assetlifecyclemanagement.employee.dto.EmployeeCreateRequestDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeResponseDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeUpdateRequestDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, // Important for partial updates.
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface EmployeeMapper {

    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE") // Set default status if not provided
//    @Mapping(target = "department.getDeptName", source = "department")
    EmployeeEntity toEntity(EmployeeCreateRequestDTO dto);

    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "firstName", ignore = true)    // Cannot change
    @Mapping(target = "lastName", ignore = true)     // Cannot change
//    @Mapping(target = "department.getDeptName", source = "department")
    EmployeeEntity updateEntity(@MappingTarget EmployeeEntity entity, EmployeeUpdateRequestDTO dto);

    EmployeeResponseDTO toResponseDto(EmployeeEntity entity);

    default List<EmployeeResponseDTO> mapList(List<EmployeeEntity> entityList) {
        return entityList.stream().map(this::toResponseDto).toList();
    }

}
