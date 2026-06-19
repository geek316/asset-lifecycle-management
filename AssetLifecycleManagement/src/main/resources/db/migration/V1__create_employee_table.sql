-- V1__create_employee_table.sql
CREATE TABLE IF NOT EXISTS employee (
                                        emp_id BIGSERIAL PRIMARY KEY,
                                        first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email_id VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    department VARCHAR(100),
    designation VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    date_of_joining DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes for better query performance
CREATE INDEX idx_employee_email ON employee(email_id);
CREATE INDEX idx_employee_department ON employee(department);
CREATE INDEX idx_employee_status ON employee(status);
CREATE INDEX idx_employee_role ON employee(role);

-- Create trigger function for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

-- Create trigger to automatically update updated_at on row update
CREATE TRIGGER update_employee_updated_at
    BEFORE UPDATE ON employee
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Optional: Add a comment to the table for documentation
COMMENT ON TABLE employee IS 'Stores employee information including authentication details and role-based access control';
COMMENT ON COLUMN employee.role IS 'User role for authorization (USER, ADMIN, MANAGER, etc.)';
COMMENT ON COLUMN employee.status IS 'Employee status (ACTIVE, INACTIVE, SUSPENDED, etc.)';