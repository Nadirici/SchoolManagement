<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Management</title>
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}">Back to Dashboard Index</a>
</p>

<h1>Student Management</h1>

<!-- Form to add a new student -->
<h2>Add a New Student</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/students">
    <label for="firstname">First Name:</label>
    <input type="text" id="firstname" name="firstname" required /><br />

    <label for="lastname">Last Name:</label>
    <input type="text" id="lastname" name="lastname" required /><br />

    <label for="dateOfBirth">Date of Birth:</label>
    <input type="date" id="dateOfBirth" name="dateOfBirth" required /><br />

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required /><br />

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required /><br />

    <button type="submit">Add Student</button>
</form>

<hr />

<!-- List of students -->
<h2>Current Students</h2>
<c:if test="${not empty students}">
    <table border="1">
        <thead>
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Date of Birth</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.firstname}</td>
                <td>${student.lastname}</td>
                <td>${student.dateOfBirth}</td>
                <td>${student.email}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/students?id=${student.id}">View Details</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty students}">
    <p>No students found.</p>
</c:if>
</body>
</html>
