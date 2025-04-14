-- USERS TABLE
CREATE TABLE Users (
    users_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    active BOOLEAN,
    created_at DATETIME
);

-- CREATORS TABLE
CREATE TABLE Creators (
    creators_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100),
    bio TEXT,
    social_media_links TEXT,
    follower_count INT,
    pricing DECIMAL(10,2),
    media_kit_path VARCHAR(255),
    niche VARCHAR(100),
    users_id INT,
    FOREIGN KEY (users_id) REFERENCES Users(users_id) ON DELETE CASCADE
);

-- BUSINESSES TABLE
CREATE TABLE Businesses (
    businesses_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150),
    description TEXT,
    website VARCHAR(255),
    industry VARCHAR(100),
    logo_path VARCHAR(255),
    users_id INT,
    FOREIGN KEY (users_id) REFERENCES Users(users_id) ON DELETE CASCADE
);

-- CAMPAIGNS TABLE
CREATE TABLE Campaigns (
    campaigns_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150),
    description TEXT,
    budget DECIMAL(12,2),
    requirements TEXT,
    deadline DATE,
    status VARCHAR(50),
    image_path VARCHAR(255),
    created_at DATETIME,
    businesses_id INT,
    FOREIGN KEY (businesses_id) REFERENCES Businesses(businesses_id) ON DELETE CASCADE
);

-- APPLICATIONS TABLE
CREATE TABLE Applications (
    applications_id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50),
    created_at DATETIME,
    proposal TEXT,
    creators_id INT,
    campaigns_id INT,
    FOREIGN KEY (creators_id) REFERENCES Creators(creators_id) ON DELETE CASCADE,
    FOREIGN KEY (campaigns_id) REFERENCES Campaigns(campaigns_id) ON DELETE CASCADE
);

-- MESSAGES TABLE
CREATE TABLE Messages (
    messages_id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    sent_at DATETIME
);

-- MESSAGES_USERS JOIN TABLE
CREATE TABLE Messages_users (
    messages_id INT,
    users_id INT,
    PRIMARY KEY (messages_id, users_id),
    FOREIGN KEY (messages_id) REFERENCES Messages(messages_id) ON DELETE CASCADE,
    FOREIGN KEY (users_id) REFERENCES Users(users_id) ON DELETE CASCADE
);

-- REPORTS TABLE
CREATE TABLE Reports (
    reports_id INT AUTO_INCREMENT PRIMARY KEY,
    reason TEXT,
    status VARCHAR(50),
    created_at DATETIME,
    resolved_at DATETIME
);

-- REPORTS_USERS JOIN TABLE
CREATE TABLE Reports_users (
    reports_id INT,
    users_id INT,
    PRIMARY KEY (reports_id, users_id),
    FOREIGN KEY (reports_id) REFERENCES Reports(reports_id) ON DELETE CASCADE,
    FOREIGN KEY (users_id) REFERENCES Users(users_id) ON DELETE CASCADE
);
