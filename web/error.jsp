<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
    <h1>Error Page</h1>
    
    <p><strong>Error Details:</strong></p>
    <p><%= exception %></p>
    
    <p><strong>Stack Trace:</strong></p>
    <pre>
        <c:forEach var="stackTraceElement" items="${exceptionStackTrace}">
            ${stackTraceElement}<br/>
        </c:forEach>
    </pre>
</body>
</html>

