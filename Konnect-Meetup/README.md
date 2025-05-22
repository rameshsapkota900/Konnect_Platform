# Konnect - A Platform to Connect Content Creators and Businesses

Konnect is a web application that bridges the gap between social media influencers and brands, facilitating promotional collaborations.

## Project Overview

This project is built using core Java technologies:
- JSP for view rendering
- Servlets for backend logic
- JDBC for database communication
- MySQL for storage
- HTML/CSS/JS for frontend (No libraries or frameworks)

## Project Structure

```
Konnect-Meetup/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── konnect/
│   │   │           ├── dao/       # Data Access Objects
│   │   │           ├── model/     # Model classes
│   │   │           ├── servlet/   # Servlet controllers
│   │   │           └── util/      # Utility classes
│   │   ├── resources/
│   │   │   └── database.sql       # Database schema
│   │   └── webapp/
│   │       ├── admin/             # Admin pages
│   │       ├── business/          # Business pages
│   │       ├── creator/           # Creator pages
│   │       ├── css/               # CSS files
│   │       ├── js/                # JavaScript files
│   │       ├── WEB-INF/
│   │       │   └── lib/           # JAR dependencies
│   │       ├── index.jsp          # Home page
│   │       ├── login.jsp          # Login page
│   │       └── register.jsp       # Registration page
└── README.md                      # Project documentation
```

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Tomcat 10 or higher
- MySQL 8.0 or higher

### Database Setup
1. Open MySQL command line or a MySQL client
2. Run the SQL script located at `src/main/resources/database.sql`

### Project Setup
1. Clone the repository
2. Import the project into Eclipse as a Dynamic Web Project
3. Add the MySQL Connector JAR to the project's lib folder
4. Configure Tomcat server in Eclipse
5. Deploy the project to Tomcat

### Default Users
The database script creates the following default users:

#### Admin
- Username: admin
- Email: admin@konnect.com
- Password: admin123

#### Creators
- Username: techcreator
- Email: tech@creator.com
- Password: password123

- Username: beautyguru
- Email: beauty@creator.com
- Password: password123

- Username: fitnesstrainer
- Email: fitness@creator.com
- Password: password123

#### Businesses
- Username: techcompany
- Email: tech@business.com
- Password: password123

- Username: beautybrands
- Email: beauty@business.com
- Password: password123

- Username: fitnessproducts
- Email: fitness@business.com
- Password: password123

## Features

### Content Creator
- Register/Login (role=creator)
- Profile Management
- Browse Campaigns
- Apply to Campaigns
- Application Tracking
- Message Business Owners
- Report a Business User

### Business Owner
- Register/Login (role=business)
- Create/Edit/Delete Campaigns
- Browse Creators
- Invite/Approve Creators
- Message Creators
- Report Creators

### Admin Panel
- Admin Login
- View All Campaigns
- User Management
- Handle Reports
- View Statistics
