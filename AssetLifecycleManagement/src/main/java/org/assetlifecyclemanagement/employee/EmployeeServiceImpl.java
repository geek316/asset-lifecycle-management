package org.assetlifecyclemanagement.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assetlifecyclemanagement.employee.dto.EmployeeCreateRequestDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeResponseDTO;
import org.assetlifecyclemanagement.employee.dto.EmployeeUpdateRequestDTO;
import org.assetlifecyclemanagement.utilities.PaginatedResponse;
import org.assetlifecyclemanagement.exception.DuplicateEmployeeException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final String CACHE_EMPLOYEES = "employees";

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {

        log.info("Fetching All employees");

        Page<EmployeeEntity> employeeEntityPage = employeeRepository.findAll(pageable);
        List<EmployeeResponseDTO> employeeResponseDtoList = employeeMapper.mapList(employeeEntityPage.getContent());

        Page<EmployeeResponseDTO> dtoPage = new PageImpl<>(employeeResponseDtoList, employeeEntityPage.getPageable(), employeeEntityPage.getTotalElements());

        return new PaginatedResponse<>(dtoPage);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = CACHE_EMPLOYEES, key = "#employeeId")
    public EmployeeResponseDTO getEmployee(Long employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.getReferenceById(employeeId);
        EmployeeResponseDTO responseDto = employeeMapper.toResponseDto(employeeEntity);
        log.info("Found employee: {} with employeeId: {}", responseDto.getFullName(), responseDto.getEmployeeId());
        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeResponseDTO> getEmployeesByFilters(String department, String status) {

        log.info("Searching employees with department {} and status {}", department, status);

        Specification<EmployeeEntity> specification = Specification     // have to read more on JPA Specification Executor.
                .where(EmployeeSpecification.hasDepartment(department))
                .and(EmployeeSpecification.hasStatus(status));

        List<EmployeeEntity> employeeEntities = employeeRepository.findAll(specification);

        return employeeMapper.mapList(employeeEntities);
    }

    @Transactional
    @Override
    @CachePut(value = CACHE_EMPLOYEES, key = "#result.employeeId")
    public EmployeeResponseDTO createEmployee(EmployeeCreateRequestDTO dto) {
        log.info("Creating new employee with email: {}", dto.getEmail());

        if (Boolean.TRUE.equals(employeeRepository.existsByEmail(dto.getEmail()))) {
            throw new DuplicateEmployeeException("email", dto.getEmail());
        }
        EmployeeEntity employeeEntity = employeeMapper.toEntity(dto);
        employeeEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);
        EmployeeResponseDTO responseDto = employeeMapper.toResponseDto(savedEmployee);
        log.info("Employee: {} created successfully with employeeId: {}", responseDto.getFullName(), responseDto.getEmployeeId());
        return employeeMapper.toResponseDto(savedEmployee);
    }

    @Transactional
    @Override
    @CachePut(value = CACHE_EMPLOYEES, key = "#result.employeeId")
    public EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeUpdateRequestDTO dto) {

        EmployeeEntity existingEmployee = employeeRepository.getReferenceById(employeeId);

        EmployeeEntity updatedEmployee = employeeMapper.updateEntity(existingEmployee, dto);
        if (dto.getPassword() != null) {
            updatedEmployee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        employeeRepository.save(updatedEmployee);
        log.info("Employee updated successfully with employeeId: {}", employeeId);
        return employeeMapper.toResponseDto(updatedEmployee);
    }

    @Transactional
    @Override
    @CacheEvict(value = CACHE_EMPLOYEES, key = "#employeeId")
    public void deleteEmployee(Long employeeId) {

        EmployeeEntity existingEmployee = employeeRepository.getReferenceById(employeeId);

        employeeRepository.delete(existingEmployee);
        log.info("Employee with employeeId:{} deleted successfully", employeeId);
    }

}
