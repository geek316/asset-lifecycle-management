package org.assetlifecyclemanagement.department;

import org.assetlifecyclemanagement.department.dto.DepartmentRequestDto;
import org.assetlifecyclemanagement.department.dto.DepartmentResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, // Important for partial updates.
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface DepartmentMapper {

    DepartmentResponseDto map(Departments entity);

    Departments map(DepartmentRequestDto dto);

    void updateDept(@MappingTarget Departments entity, DepartmentRequestDto dto);

    default List<DepartmentResponseDto> mapList(List<Departments> entityList) {
        return entityList.stream().map(this::map).toList();
    }

}
