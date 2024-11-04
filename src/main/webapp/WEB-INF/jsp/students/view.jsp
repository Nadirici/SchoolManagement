<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Details</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<h1>Student Details</h1>

<table>
    <tr>
        <th>ID:</th>
        <td>${student.id}</td>
    </tr>
    <tr>
        <th>First Name:</th>
        <td>${student.firstname}</td>
    </tr>
    <tr>
        <th>Last Name:</th>
        <td>${student.lastname}</td>
    </tr>
    <tr>
        <th>Email:</th>
        <td>${student.email}</td>
    </tr>
</table>

<a href="${student.id}/courses">View Courses</a> |
<a href="/admin/students">Back to List</a>
</body>
</html>
