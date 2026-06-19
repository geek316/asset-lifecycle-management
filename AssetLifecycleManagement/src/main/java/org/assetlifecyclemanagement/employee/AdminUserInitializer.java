package org.assetlifecyclemanagement.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assetlifecyclemanagement.enums.Status;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    public void createAdminUser() {

        String adminEmail = "admin@example.com";

        if (Boolean.FALSE.equals(employeeRepository.existsByEmail(adminEmail))) {
            EmployeeEntity admin = new EmployeeEntity();
            admin.setEmail(adminEmail);
            admin.setFirstName("Admin");  // Keep short
            admin.setLastName("User");    // Keep short
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setDepartment("IT");      // Short - use "IT", "HR", "FIN", etc.
            admin.setDesignation("Admin");   // Short - use "Admin", "Manager", etc.
            admin.setPhoneNumber("1234567890"); // 10 digits
            admin.setDateOfJoining(LocalDate.of(2026, 1, 1));
            admin.setStatus(Status.ACTIVE);      // Short

            employeeRepository.save(admin);
            log.info("Admin user created successfully");
        } else {
            log.info("Admin user already exists");
        }
    }
}