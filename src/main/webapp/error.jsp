<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Konnect Platform</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <style>
        .error-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        .error-icon {
            font-size: 4em;
            color: #dc3545;
            margin-bottom: 20px;
        }
        .error-title {
            color: #dc3545;
            margin-bottom: 20px;
            font-size: 1.5em;
        }
        .error-message {
            color: #6c757d;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        .error-id {
            background: #f8f9fa;
            padding: 10px;
            border-radius: 5px;
            font-family: monospace;
            margin: 20px 0;
            font-size: 0.9em;
            color: #495057;
        }
        .action-buttons {
            margin-top: 30px;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin: 0 10px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #545b62;
            color: white;
        }
        .error-details {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #dee2e6;
            font-size: 0.9em;
            color: #6c757d;
        }
        @media (max-width: 768px) {
            .error-container {
                margin: 20px;
                padding: 20px;
            }
            .btn {
                display: block;
                margin: 10px 0;
            }
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        
        <h1 class="error-title">
            <% 
                Integer statusCode = (Integer) request.getAttribute("statusCode");
                if (statusCode != null && statusCode == 404) {
                    out.print("Page Not Found");
                } else if (statusCode != null && statusCode >= 500) {
                    out.print("Server Error");
                } else {
                    out.print("An Error Occurred");
                }
            %>
        </h1>
        
        <div class="error-message">
            <% 
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null && !errorMessage.trim().isEmpty()) {
                    out.print(errorMessage);
                } else {
                    out.print("We're sorry, but something went wrong. Please try again later.");
                }
            %>
        </div>
        
        <% 
            String errorId = (String) request.getAttribute("errorId");
            if (errorId != null && !errorId.trim().isEmpty()) {
        %>
        <div class="error-id">
            <strong>Error ID:</strong> <%= errorId %>
            <br><small>Please include this ID when contacting support</small>
        </div>
        <% } %>
        
        <div class="action-buttons">
            <a href="javascript:history.back()" class="btn btn-secondary">Go Back</a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Home Page</a>
        </div>
        
        <div class="error-details">
            <% 
                String errorContext = (String) request.getAttribute("errorContext");
                if (errorContext != null && !errorContext.trim().isEmpty()) {
            %>
            <p><strong>Context:</strong> <%= errorContext %></p>
            <% } %>
            
            <% 
                if (statusCode != null) {
            %>
            <p><strong>Status Code:</strong> <%= statusCode %></p>
            <% } %>
            
            <p><strong>Timestamp:</strong> <%= new java.util.Date() %></p>
            
            <p style="margin-top: 20px;">
                If this problem persists, please contact our support team at 
                <a href="mailto:support@konnect.com">support@konnect.com</a>
            </p>
        </div>
    </div>
</body>
</html>