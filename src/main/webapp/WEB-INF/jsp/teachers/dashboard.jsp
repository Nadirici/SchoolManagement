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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <%@include file="../headers/teacher_header.jsp"%>
    <div class="main-content">

        <div class="overviewStudent">
            <h2>Bonjour ${teacher.lastname} ${teacher.firstname} !</h2>
            <div class="stats">
                <div class="stat-card">
                    <h2>Mes infos personnelles</h2>
                    <ul>
                        <li>Nom : ${teacher.lastname}</li>
                        <li>Prénom : ${teacher.firstname}</li>
                        <li>Département : ${teacher.department}</li>
                        <li>Email : ${teacher.email}</li>
                    </ul>
                 </div>
            <div>

                <h2>Mes cours enseignés</h2>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Moyenne de la classe</th>
                        <th>Moyenne minimale</th>
                        <th>Moyenne maximale</th>
                        <th>Plus d'info</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td><a href="${teacher.id}/courses/${course.id}">${course.name}</a></td>
                            <td><c:out value="${courseAverages[course.id]}" /></td>
                            <td><c:out value="${minAverages[course.id]}" /></td>
                            <td><c:out value="${maxAverages[course.id]}" /></td>
                            <td><a href="${teacher.id}/courses/${course.id}" class="button">Voir plus</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    </div>
</div>
</body>
</html>
