package org.assetlifecyclemanagement.utilities;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assetlifecyclemanagement.employee.EmployeeEntity;
import org.assetlifecyclemanagement.employee.EmployeeRepository;
import org.assetlifecyclemanagement.enums.Role;
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
        String userEmail = "user@example.com";

        if (Boolean.FALSE.equals(employeeRepository.existsByEmail(adminEmail))) {
            EmployeeEntity admin = new EmployeeEntity();
            admin.setEmail(adminEmail);
            admin.setFirstName("Admin");  // Keep short
            admin.setLastName("User");    // Keep short
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setDepartment("IT");      // Short - use "IT", "HR", "FIN", etc.
            admin.setDesignation("Administrator");
            admin.setRole(Role.ADMIN);   // Short - use "Admin", "Manager", etc.
            admin.setPhoneNumber("1234567890"); // 10 digits
            admin.setDateOfJoining(LocalDate.of(2026, 1, 1));
            admin.setStatus(Status.ACTIVE);      // Short

            employeeRepository.save(admin);
            log.info("Admin created successfully");
        } else {
            EmployeeEntity existingAdmin = employeeRepository.findByEmail(adminEmail).orElse(null);
            if (existingAdmin != null && existingAdmin.getRole() != Role.ADMIN) {
                existingAdmin.setRole(Role.ADMIN);
                employeeRepository.save(existingAdmin);
                log.info("Updated existing admin user role to ADMIN");
            } else {
                log.info("Admin already exists with correct role");
            }
        }

        if (Boolean.FALSE.equals(employeeRepository.existsByEmail(userEmail))) {
            EmployeeEntity user = new EmployeeEntity();
            user.setEmail(userEmail);
            user.setFirstName("User");  // Keep short
            user.setLastName("User");    // Keep short
            user.setPassword(passwordEncoder.encode("password"));
            user.setDepartment("IT");      // Short - use "IT", "HR", "FIN", etc.
            user.setDesignation("User");
            user.setRole(Role.USER);   // Short - use "Admin", "Manager", etc.
            user.setPhoneNumber("1234567890"); // 10 digits
            user.setDateOfJoining(LocalDate.of(2026, 1, 1));
            user.setStatus(Status.ACTIVE);      // Short

            employeeRepository.save(user);
            log.info("User created successfully");
        } else {
            log.info("User already exists");
        }
    }
}