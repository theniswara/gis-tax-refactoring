-- Admin table creation and initial data
-- Run this after starting the application for the first time

-- Create admins table (if not auto-created by Hibernate)
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Insert default admin account
-- Username: admin
-- Password: admin123 (CHANGE THIS IMMEDIATELY!)
-- This password is BCrypt hashed with strength 12
INSERT INTO admins (username, email, password, full_name, role, is_active)
VALUES (
    'admin',
    'admin@university.edu',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5gyFUHYPKKWES',  -- password: admin123
    'System Administrator',
    'ADMIN',
    TRUE
)
ON DUPLICATE KEY UPDATE username=username;

-- Create indexes for better query performance
CREATE INDEX idx_admin_username ON admins(username);
CREATE INDEX idx_admin_email ON admins(email);
CREATE INDEX idx_admin_active ON admins(is_active);

-- Add role column to students table if not exists
ALTER TABLE students 
ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'STUDENT';

-- Update existing students to have STUDENT role
UPDATE students SET role = 'STUDENT' WHERE role IS NULL OR role = '';

-- Create index on student role
CREATE INDEX IF NOT EXISTS idx_student_role ON students(role);
