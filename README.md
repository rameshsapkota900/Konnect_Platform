# Konnect Platform - Setup Guide for IntelliJ IDEA

## 🚀 Quick Setup Instructions

### Prerequisites
- **Java 21** or higher
- **IntelliJ IDEA** (Community or Ultimate)
- **PostgreSQL 14** or higher
- **Apache Tomcat 10** or higher
- **Maven** (included with IntelliJ)

### 1. 📁 Import Project in IntelliJ IDEA

1. Open IntelliJ IDEA
2. Click **File** → **Open**
3. Navigate to the project directory and select the `pom.xml` file
4. Click **Open as Project**
5. Wait for Maven to download dependencies

### 2. 🗄️ Database Setup

#### Create Database
```sql
-- Run this in pgAdmin or psql command line
CREATE DATABASE "Konnect_Platform";
```

#### Import Schema
1. Open pgAdmin or use psql command line
2. Connect to the "Konnect_Platform" database
3. Run the SQL script: `src/main/resources/sql/konnect_postgresql.sql`

#### Using psql command line:
```bash
psql -U postgres -d "Konnect_Platform" -f src/main/resources/sql/konnect_postgresql.sql
```

#### Configure Database Connection
1. Edit `src/main/resources/database.properties`
2. Update the following properties if needed:
```properties
db.url=jdbc:postgresql://localhost:5432/Konnect_Platform
db.username=postgres
db.password=12345
```

### 3. 🖥️ Configure Tomcat in IntelliJ IDEA

#### Add Tomcat Server
1. Go to **Run** → **Edit Configurations**
2. Click **+** → **Tomcat Server** → **Local**
3. Configure:
   - **Name**: Konnect Platform
   - **Application server**: Browse to your Tomcat installation
   - **JRE**: Use Java 21

#### Configure Deployment
1. In the same dialog, go to **Deployment** tab
2. Click **+** → **Artifact** → **konnect-platform:war exploded**
3. Set **Application context**: `/konnect`

#### Configure Server Settings
- **HTTP port**: 8080
- **JMX port**: 1099 (or any available port)

### 4. 🛠️ Project Configuration

#### Set Project SDK
1. Go to **File** → **Project Structure**
2. Under **Project**, set:
   - **Project SDK**: Java 21
   - **Project language level**: 21

#### Configure Modules
1. In **Project Structure** → **Modules**
2. Verify the module is correctly configured with:
   - **Sources**: `src/main/java`
   - **Resources**: `src/main/resources`
   - **Test Sources**: `src/test/java`
   - **Web**: `src/main/webapp`

### 5. 🏃‍♂️ Running the Application

#### Method 1: Using IntelliJ Run Configuration
1. Select the **Konnect Platform** configuration
2. Click the **Run** button (▶️)
3. Wait for Tomcat to start
4. Open browser: `http://localhost:8080/konnect`

#### Method 2: Using Maven
```bash
mvn clean compile
mvn tomcat7:run
```

### 6. 🧪 Testing the Setup

1. **Homepage**: `http://localhost:8080/konnect/`
2. **Login**: `http://localhost:8080/konnect/login`
3. **Register**: `http://localhost:8080/konnect/register`
4. **Admin Login**: 
   - Email: `admin@konnect.com`
   - Password: `admin`

### 7. 🔧 Common Issues & Solutions

#### Database Connection Issues
- Verify PostgreSQL is running on port 5432
- Check database credentials in `database.properties`
- Ensure database `Konnect_Platform` exists
- Test connection: `psql -U postgres -d "Konnect_Platform"`

#### Tomcat Issues
- Check if port 8080 is available
- Verify Tomcat installation path
- Check server logs in IntelliJ console

#### Maven Dependencies
- Right-click project → **Maven** → **Reload project**
- Or use: **View** → **Tool Windows** → **Maven** → Refresh

#### Build Issues
- Clean and rebuild: **Build** → **Rebuild Project**
- Or use Maven: `mvn clean compile`

### 8. 📂 Project Structure

```
konnect-platform/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/konnect/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── dao/             # Data Access Objects
│   │   │       ├── model/           # Entity models
│   │   │       ├── servlet/         # Controllers
│   │   │       └── util/            # Utility classes
│   │   ├── resources/
│   │   │   ├── database.properties  # Database config
│   │   │   └── sql/                 # SQL scripts
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml          # Web configuration
│   │       ├── css/                 # Stylesheets
│   │       ├── images/              # Static images
│   │       └── *.jsp                # JSP pages
│   └── test/                        # Test classes
└── target/                          # Build output
```

### 9. 🎯 Next Steps

- [ ] Set up debugging breakpoints
- [ ] Configure database connection pooling
- [ ] Set up unit tests
- [ ] Configure logging (Log4j or SLF4J)
- [ ] Set up development vs production profiles

### 🔒 Security Notes

- Change default admin password after first login
- Update email SMTP credentials in `database.properties`
- Consider using environment variables for sensitive data

### 📞 Support

If you encounter any issues:
1. Check the IntelliJ IDEA console for error messages
2. Verify all prerequisites are installed
3. Check database connectivity
4. Review Tomcat server logs

---

**Happy Coding! 🚀**