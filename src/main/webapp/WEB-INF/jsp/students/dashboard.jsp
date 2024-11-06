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
    <title>Tableau de bord</title>
</head>
<body>
    <h1>Bonjour ${student.firstname} !</h1>
    <div>
        <h2>Mes infos personnelles</h2>
        <ul>
            <li>Nom : ${student.lastname}</li>
            <li>Prénom : ${student.firstname}</li>
            <li>Date de naissance : ${student.dateOfBirth}</li>
            <li>Email : ${student.email}</li>
        </ul>
    </div>
    <div>
        <h2>Mes cours</h2>
        <table border="1">
            <thead>
            <tr>
                <th>Nom</th>
                <th>Ma moyenne</th>
                <th>Moyenne de la classe</th>
                <th>Moyenne minimale</th>
                <th>Moyenne maximale</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="course" items="${courses}">
                <tr>
                    <td><a href="${student.id}/courses/${course.id}">${course.name}</a></td>
                    <td><%--${course.myAverage}--%></td>
                    <td><%--${course.classAverage}--%></td>
                    <td><%--${course.minAverage}--%></td>
                    <td><%--${course.maxAverage}--%></td>
                </tr>
            </c:forEach>
            <tr>
                <th>Moyenne générale</th>
                <td><%--${generalMyAverage}--%></td>
                <td><%--${generalClassAverage}--%></td>
                <td><%--${generalMinAverage}--%></td>
                <td><%--${generalMaxAverage}--%></td>
            </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
