package org.assetlifecyclemanagement.department;

import org.assetlifecyclemanagement.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Long> {

    Optional<Departments> findByDeptCode(Department deptCode);

}
