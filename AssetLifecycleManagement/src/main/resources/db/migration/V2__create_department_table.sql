-- =============================================
-- Table: department
-- =============================================
CREATE TABLE IF NOT EXISTS department (
                                          dept_id BIGSERIAL PRIMARY KEY,
                                          department_code VARCHAR(50) NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
    );

-- Add comments for documentation
COMMENT ON TABLE department IS 'Department master table';
COMMENT ON COLUMN department.dept_id IS 'Primary key - auto-generated department ID';
COMMENT ON COLUMN department.department_code IS 'Unique department code (e.g., IT, HR, FIN)';
COMMENT ON COLUMN department.department_name IS 'Department full name';
COMMENT ON COLUMN department.created_at IS 'Audit: Record creation timestamp';
COMMENT ON COLUMN department.updated_at IS 'Audit: Record last update timestamp';

-- Create indexes for better performance
CREATE INDEX idx_department_code ON department(department_code);
CREATE INDEX idx_department_name ON department(department_name);
CREATE INDEX idx_department_created_at ON department(created_at);
CREATE INDEX idx_department_updated_at ON department(updated_at);