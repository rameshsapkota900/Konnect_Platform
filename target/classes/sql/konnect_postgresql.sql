-- Konnect Platform PostgreSQL Complete SQL Script
-- This script creates the database, tables, and adds sample data

-- Create database (run this separately or ensure database exists)
-- CREATE DATABASE "Konnect_Platform";

-- Connect to the database
-- \c "Konnect_Platform";

-- Drop existing tables if they exist (in correct order to avoid foreign key constraints)
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS campaign_interests CASCADE;
DROP TABLE IF EXISTS campaigns CASCADE;
DROP TABLE IF EXISTS creator_interests CASCADE;
DROP TABLE IF EXISTS creator_profiles CASCADE;
DROP TABLE IF EXISTS business_profiles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'creator', 'business')),
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('active', 'pending', 'banned', 'inactive')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    verification_code VARCHAR(255) NULL,
    verification_expiry TIMESTAMP NULL,
    reset_token VARCHAR(255) NULL,
    reset_token_expiry TIMESTAMP NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create function to update the updated_at column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for users table
CREATE TRIGGER update_users_updated_at BEFORE UPDATE
    ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create creator_profiles table
CREATE TABLE creator_profiles (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    bio TEXT,
    follower_count INTEGER DEFAULT 0,
    instagram_link VARCHAR(255),
    tiktok_link VARCHAR(255),
    youtube_link VARCHAR(255),
    pricing_per_post DECIMAL(10, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create trigger for creator_profiles table
CREATE TRIGGER update_creator_profiles_updated_at BEFORE UPDATE
    ON creator_profiles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create interests table for creators
CREATE TABLE creator_interests (
    id SERIAL PRIMARY KEY,
    creator_id INTEGER NOT NULL,
    interest VARCHAR(50) NOT NULL,
    FOREIGN KEY (creator_id) REFERENCES creator_profiles(id) ON DELETE CASCADE,
    UNIQUE (creator_id, interest)
);

-- Create business_profiles table
CREATE TABLE business_profiles (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    company_name VARCHAR(100) NOT NULL,
    industry VARCHAR(50),
    description TEXT,
    website VARCHAR(255),
    contact_phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create trigger for business_profiles table
CREATE TRIGGER update_business_profiles_updated_at BEFORE UPDATE
    ON business_profiles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create campaigns table
CREATE TABLE campaigns (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT,
    budget DECIMAL(10, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'draft' CHECK (status IN ('draft', 'active', 'completed', 'cancelled')),
    min_followers INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (business_id) REFERENCES business_profiles(id) ON DELETE CASCADE
);

-- Create trigger for campaigns table
CREATE TRIGGER update_campaigns_updated_at BEFORE UPDATE
    ON campaigns FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create campaign_interests table
CREATE TABLE campaign_interests (
    id SERIAL PRIMARY KEY,
    campaign_id INTEGER NOT NULL,
    interest VARCHAR(50) NOT NULL,
    FOREIGN KEY (campaign_id) REFERENCES campaigns(id) ON DELETE CASCADE,
    UNIQUE (campaign_id, interest)
);

-- Create applications table
CREATE TABLE applications (
    id SERIAL PRIMARY KEY,
    campaign_id INTEGER NOT NULL,
    creator_id INTEGER NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected', 'completed', 'withdrawn')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (campaign_id) REFERENCES campaigns(id) ON DELETE CASCADE,
    FOREIGN KEY (creator_id) REFERENCES creator_profiles(id) ON DELETE CASCADE,
    UNIQUE (campaign_id, creator_id)
);

-- Create trigger for applications table
CREATE TRIGGER update_applications_updated_at BEFORE UPDATE
    ON applications FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create reports table
CREATE TABLE reports (
    id SERIAL PRIMARY KEY,
    reporter_id INTEGER NOT NULL,
    reported_id INTEGER NOT NULL,
    report_type VARCHAR(20) NOT NULL CHECK (report_type IN ('user', 'campaign', 'application')),
    reason TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'resolved', 'dismissed')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reported_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create trigger for reports table
CREATE TRIGGER update_reports_updated_at BEFORE UPDATE
    ON reports FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create messages table
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_creator_profiles_user_id ON creator_profiles(user_id);
CREATE INDEX idx_business_profiles_user_id ON business_profiles(user_id);
CREATE INDEX idx_campaigns_business_id ON campaigns(business_id);
CREATE INDEX idx_campaigns_status ON campaigns(status);
CREATE INDEX idx_applications_campaign_id ON applications(campaign_id);
CREATE INDEX idx_applications_creator_id ON applications(creator_id);
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_receiver_id ON messages(receiver_id);
CREATE INDEX idx_messages_is_read ON messages(is_read);

-- Insert sample data
-- Create admin user with SHA-256 hashed passwords
-- Password 'admin' = 8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
-- Password 'password' = 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8

INSERT INTO users (username, email, password, role, status, verified) VALUES
('admin', 'admin@konnect.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin', 'active', TRUE);

-- Insert sample creator user
INSERT INTO users (username, email, password, role, status, verified) VALUES
('creator_demo', 'creator@demo.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'creator', 'active', TRUE);

-- Insert sample business user
INSERT INTO users (username, email, password, role, status, verified) VALUES
('business_demo', 'business@demo.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'business', 'active', TRUE);

-- Insert creator profile for demo creator
INSERT INTO creator_profiles (user_id, bio, follower_count, instagram_link, tiktok_link, youtube_link, pricing_per_post) VALUES
(2, 'Demo content creator specializing in tech reviews and tutorials', 10000, 'https://instagram.com/creator_demo', 'https://tiktok.com/@creator_demo', 'https://youtube.com/creator_demo', 500.00);

-- Insert business profile for demo business
INSERT INTO business_profiles (user_id, company_name, industry, description, website, contact_phone) VALUES
(3, 'Demo Tech Company', 'Technology', 'A demo technology company showcasing innovative products', 'https://demotech.com', '+1-555-0123');

-- Insert creator interests
INSERT INTO creator_interests (creator_id, interest) VALUES
(1, 'Technology'),
(1, 'Gaming'),
(1, 'Reviews');

-- Display completion message
SELECT 'Database setup completed successfully!' as message;

-- Display table counts
SELECT 
    'users' as table_name, COUNT(*) as record_count FROM users
UNION ALL
SELECT 
    'creator_profiles' as table_name, COUNT(*) as record_count FROM creator_profiles
UNION ALL
SELECT 
    'business_profiles' as table_name, COUNT(*) as record_count FROM business_profiles;