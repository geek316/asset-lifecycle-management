-- V1__create_employee_table.sql
CREATE TABLE IF NOT EXISTS employee (
                                        emp_id BIGSERIAL PRIMARY KEY,
                                        first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email_id VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  -- Added password column with sufficient length
    phone_number VARCHAR(20),
    department VARCHAR(100),
    designation VARCHAR(100) NOT NULL,
    date_of_joining DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes
CREATE INDEX idx_email ON employee(email_id);
CREATE INDEX idx_department ON employee(department);
CREATE INDEX idx_status ON employee(status);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_employee_updated_at
    BEFORE UPDATE ON employee
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();