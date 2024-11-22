<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Teacher Management</title>
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}">Back to Dashboard Index</a>
</p>

<h1>Teacher Management</h1>

<!-- Form to add a new student -->
<h2>Add a New Teacher</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/teachers">
    <label for="firstname">First Name:</label>
    <input type="text" id="firstname" name="firstname" required /><br />

    <label for="lastname">Last Name:</label>
    <input type="text" id="lastname" name="lastname" required /><br />

    <label for="department">Department</label>
    <select id="department" name="department">
        <option value="" disabled selected>-- Choisissez votre departement --</option>
        <option value="INFORMATIQUE">Informatique</option>
        <option value="MATHEMATIQUES">Mathematiques</option>
        <option value="PHYSIQUE">Physique</option>
        <option value="CHIMIE">Chimie</option>
    </select><br />

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required /><br />

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required /><br />

    <button type="submit">Add Teacher</button>
</form>

<hr />

<!-- List of students -->
<h2>Current Teachers</h2>
<c:if test="${not empty teachers}">
    <table border="1">
        <thead>
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Department</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="teacher" items="${teachers}">
            <tr>
                <td>${teacher.id}</td>
                <td>${teacher.firstname}</td>
                <td>${teacher.lastname}</td>
                <td>${teacher.department}</td>
                <td>${teacher.email}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/teachers?id=${teacher.id}">View Details</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty teachers}">
    <p>No teachers found.</p>
</c:if>
</body>
</html>
