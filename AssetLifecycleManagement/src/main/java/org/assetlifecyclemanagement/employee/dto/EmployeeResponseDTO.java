package org.assetlifecyclemanagement.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assetlifecyclemanagement.enums.Status;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO implements Serializable {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String email;
    private String department;
    private String designation;
    private LocalDate dateOfJoining;
    private Status status;

    // Optional derived field
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
