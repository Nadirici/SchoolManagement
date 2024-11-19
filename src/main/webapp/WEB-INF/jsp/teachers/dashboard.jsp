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
<h1>Bonjour ${teacher.lastname} ${teacher.firstname} !</h1>
<div>
    <h2>Mes infos personnelles</h2>
    <ul>
        <li>Nom : ${teacher.lastname}</li>
        <li>Prénom : ${teacher.firstname}</li>
        <li>Département : ${teacher.department}</li>
        <li>Email : ${teacher.email}</li>
    </ul>
</div>
<div>

    <div>
        <h2>Créer un nouveau cours</h2>
        <form action="${teacher.id}/courses/create" method="post">
            <label for="courseName">Nom du cours :</label>
            <input type="text" id="courseName" name="name" required />
            <br />
            <label for="description">Description :</label>
            <textarea id="description" name="description" required></textarea>
            <br />
            <button type="submit">Créer le cours</button>
        </form>
    </div>
    <h2>Mes cours enseignés</h2>
    <table border="1">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Moyenne de la classe</th>
            <th>Moyenne minimale</th>
            <th>Moyenne maximale</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="course" items="${courses}">
            <tr>
                <td><a href="${teacher.id}/courses/${course.id}">${course.name}</a></td>
                <td><c:out value="${courseAverages[course.id]}" /></td>
                <td><c:out value="${minAverages[course.id]}" /></td>
                <td><c:out value="${maxAverages[course.id]}" /></td>
                <td><a href="courses/${course.id}" class="button">More</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
