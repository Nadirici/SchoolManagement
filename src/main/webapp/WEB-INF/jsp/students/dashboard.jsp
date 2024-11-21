<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 06/11/2024
  Time: 09:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tableau de bord</title>
    <link rel="stylesheet" href="/css/students/style.css">
</head>
<body>
    <h1>👋 Hi ${student.firstname} !</h1>
    <div>
        <h2>Personal Information</h2>
        <ul>
            <li>Last Name : ${student.lastname}</li>
            <li>First Name : ${student.firstname}</li>
            <li>Date of Birth : ${student.dateOfBirth}</li>
            <li>E-mail : ${student.email}</li>
        </ul>
    </div>
    <div>
        <h2>My Courses</h2>
        <table border="1">
            <thead>
            <tr>
                <th>Name</th>
                <th>My Average</th>
                <th>Class Average</th>
                <th>Minimum Average</th>
                <th>Maximum Average</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="course" items="${courses}">
                <tr>
                    <td><a href="${student.id}/courses/${course.id}">${course.name}</a></td>
                    <td><c:out value="${studentAverages[course.id]}" /></td>
                    <td><c:out value="${courseAverages[course.id]}" /></td>
                    <td><c:out value="${minAverages[course.id]}" /></td>
                    <td><c:out value="${maxAverages[course.id]}" /></td>
                </tr>
            </c:forEach>
            <tr>
                <th>General Average</th>
                <td><%--${generalMyAverage}--%></td>
                <td><%--${generalClassAverage}--%></td>
                <td><%--${generalMinAverage}--%></td>
                <td><strong><c:out value="${studentGlobalAverage}" /></strong></td>
            </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
