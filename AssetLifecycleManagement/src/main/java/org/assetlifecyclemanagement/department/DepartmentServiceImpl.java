package org.assetlifecyclemanagement.department;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assetlifecyclemanagement.department.dto.DepartmentRequestDto;
import org.assetlifecyclemanagement.department.dto.DepartmentResponseDto;
import org.assetlifecyclemanagement.enums.Department;
import org.assetlifecyclemanagement.exception.DepartmentNotFoundException;
import org.assetlifecyclemanagement.exception.DuplicateDepartmentException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.LongFunction;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentResponseDto> findAllDepartments() {
        List<Departments> existingDeptList = departmentRepository.findAll();
        log.info("Fetching all departments.");
        return departmentMapper.mapList(existingDeptList);

    }

    @Override
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto) {

        departmentRepository.findByDeptCode(dto.getDeptCode())
                .ifPresent( dept -> {
                    throw new DuplicateDepartmentException("Department already exists!");
                });

        Departments savedEntity = departmentRepository.save(departmentMapper.map(dto));
        log.info("Department {} created with id: {}", savedEntity.getDeptCode(), savedEntity.getDeptId());
        return departmentMapper.map(savedEntity);
    }

    @Override
    public DepartmentResponseDto updateDepartment(DepartmentRequestDto dto) {

        Departments existingDept = departmentRepository.findByDeptCode(dto.getDeptCode())
                .orElseThrow(() -> new DepartmentNotFoundException(dto.getDeptCode()));

        departmentMapper.updateDept(existingDept, dto);
        Departments savedEntity = departmentRepository.save(existingDept);
        log.info("Department {} updated successfully.", savedEntity.getDeptCode());

        return departmentMapper.map(savedEntity);
    }

    @Override
    public void deleteDepartment(Department deptCode) {

        Departments existingDept = departmentRepository.findByDeptCode(deptCode)
                .orElseThrow(() -> new DepartmentNotFoundException(deptCode));

        departmentRepository.delete(existingDept);
        log.info("Department {} deleted successfully.", deptCode);

    }
}
