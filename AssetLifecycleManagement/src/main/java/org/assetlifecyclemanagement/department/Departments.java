package org.assetlifecyclemanagement.department;

import jakarta.persistence.*;
import lombok.*;
import org.assetlifecyclemanagement.employee.AuditEntity;
import org.assetlifecyclemanagement.enums.Department;

@Entity
@Getter
@Setter
@Table(name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Departments extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id", nullable = false)
    private Long deptId;

    @Enumerated(EnumType.STRING)
    @Column(name = "department_code", nullable = false, unique = true)
    private Department deptCode;

    @Column(name = "department_name", nullable = false, length = 100)
    private String deptName;

//    @OneToMany(mappedBy = "department")
//    @JsonIgnore
//    private List<EmployeeEntity> employees = new ArrayList<>();

}
