<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Courses</title>
    <%--<link rel="stylesheet" href="/css/style.css">--%>
</head>
<body>
<h1>Courses for ${student.firstname} ${student.lastname}</h1>

<table>
    <thead>
    <tr>
        <th>Course ID</th>
        <th>Course Name</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="course" items="${courses}">
        <tr>
            <td>${course.id}</td>
            <td>${course.name}</td>
            <td>${course.description}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<a href="/admin/students/${student.id}">Back to Student Details</a> |
<a href="/admin/students">Back to List</a>
</body>
</html>
