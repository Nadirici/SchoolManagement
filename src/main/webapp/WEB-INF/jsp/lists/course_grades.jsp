
<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 11/10/2024
  Time: 9:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Vos devoirs</title>
</head>
<body>
    <div>
        <h2>Devoirs</h2>
        <c:if test="${canViewGrades}">
        <table border="1">
            <thead>
            <tr>
                <th>Prénom</th>
                <th>Nom</th>
                <c:forEach var="assignment" items="${assignments}">
                    <th>${assignment.title}</th>
                </c:forEach>
                <th>Moyenne</th>
            </tr>
            </thead>
            <tbody>

                <!-- Inclure la page liste.jsp -->
                <c:forEach var="student" items="${students}">
                    <tr>
                        <td>${student.firstname}</td>
                        <td>${student.lastname}</td>
                    <c:forEach var="assignment" items="${assignments}">
                        <td><c:out value="${assignmentsGrades[student.id][assignment.id]}" /></td>
                    </c:forEach>
                        <td><c:out value="${averageGrades[student.id]}" /></td>
                    </tr>
                </c:forEach>
                <a href="../${course.id}">Revenir au cours</a>



            </tbody>
        </table>
        </c:if>
    </div>
</body>
</html>
