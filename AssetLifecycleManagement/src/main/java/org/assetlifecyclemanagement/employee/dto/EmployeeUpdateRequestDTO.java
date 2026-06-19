package org.assetlifecyclemanagement.employee.dto;

import jakarta.validation.constraints.NotBlank;
import org.assetlifecyclemanagement.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequestDTO {

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Length of password must be between 8 and 255 characters")
    private String password;

    @Size(min = 10, max = 20, message = "Length of the phoneNumber should not exceed 20 characters")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    private Status status;

}
