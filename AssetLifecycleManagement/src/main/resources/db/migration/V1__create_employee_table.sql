-- V1__create_employee_table.sql
-- Create employeeEntity table for Asset Lifecycle Management

CREATE TABLE IF NOT EXISTS employeeEntity (
    emp_Id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100),
    designation VARCHAR(100),
    date_of_joining DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes for better performance
CREATE INDEX idx_email ON employeeEntity(email);
CREATE INDEX idx_department ON employeeEntity(department);
CREATE INDEX idx_status ON employeeEntity(status);

-- Create a trigger to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_employee_updated_at
    BEFORE UPDATE ON employeeEntity
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add comments to document the table and columns
COMMENT ON TABLE employeeEntity IS 'Stores employeeEntity/hardware asset management information';
COMMENT ON COLUMN employeeEntity.status IS 'Employee status: ACTIVE, INACTIVE, TERMINATED, ON_LEAVE';