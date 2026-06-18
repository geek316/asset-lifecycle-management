package org.assetlifecyclemanagement.employee.dto;

import org.assetlifecyclemanagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO implements Serializable {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String designation;
    private LocalDate dateOfJoining;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Optional derived field
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
