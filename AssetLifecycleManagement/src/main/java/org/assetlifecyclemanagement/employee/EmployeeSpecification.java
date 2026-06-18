package org.assetlifecyclemanagement.employee;

import org.springframework.data.jpa.domain.Specification;

import static java.util.Objects.isNull;

public class EmployeeSpecification {

    public static Specification<EmployeeEntity> hasDepartment(String department) {
        return (root, query, cb) -> isNull(department) ? cb.conjunction() : cb.equal(root.get("department"), department);
    }

    public static Specification<EmployeeEntity> hasStatus(String status) {
        return (root, query, cb) -> isNull(status) ? cb.conjunction() : cb.equal(root.get("status"), status);
    }
}
