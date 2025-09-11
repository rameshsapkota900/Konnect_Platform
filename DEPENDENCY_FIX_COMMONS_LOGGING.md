# 🔧 Dependency Fix - Commons Logging Missing

## Issue Description
Login was failing with a ClassNotFoundException for `org.apache.commons.logging.LogFactory` when trying to initialize BCrypt password encoder.

## Error Analysis
```
java.lang.NoClassDefFoundError: org/apache/commons/logging/LogFactory
    at org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.<init>
    at com.konnect.util.PasswordUtil.<clinit>
```

**Root Cause**: Spring Security BCrypt library requires Apache Commons Logging, but it wasn't included in dependencies.

## Solution Implemented

### Added Missing Dependency
Added to `pom.xml`:
```xml
<!-- Apache Commons Logging (required by Spring Security) -->
<dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.3.0</version>
</dependency>
```

### Why This Was Needed
- **Spring Security BCrypt** depends on Commons Logging internally
- **Transitive Dependencies** weren't properly resolved in our Maven configuration
- **Runtime ClassLoader** couldn't find the required logging classes

## Fix Status
✅ **RESOLVED**: Commons logging dependency added  
✅ **TESTED**: Build successful with no errors  
✅ **READY**: BCrypt password encoder should now initialize properly  

The admin login should now work correctly with proper password hashing and migration!