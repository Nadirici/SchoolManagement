<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 06/11/2024
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${course.name}</title>
</head>
<body>
    <h1>${course.name}</h1>
    <p>Description : ${course.description}</p>
    <p>Enseignant : ${course.teacher.firstname} ${course.teacher.lastname}</p>
    <div>
        <h2>Mes notes</h2>
        <table border="1">
            <thead>
            <tr>
                <th>Nom de l'évaluation</th>
                <th>Note</th>
                <th>Note minimale</th>
                <th>Note maximale</th>
                <th>Moyenne de la classe</th>
            </tr>
            </thead>
            <tbody>
            <!-- Boucle pour afficher chaque note -->
            <c:forEach var="grade" items="${grades}">
                <tr>
                    <td><%--${grade.name}--%></td>
                    <td>${grade.value}</td>
                    <td><%--${grade.minScore}--%></td>
                    <td><%--${grade.maxScore}--%></td>
                    <td><%--${grade.classAverage}--%></td>
                </tr>
            </c:forEach>

            <!-- Ligne pour la moyenne générale -->
            <tr>
                <th>Moyenne générale</th>
                <td><%--${generalAverage}--%></td>
                <td><%--${generalMinScore}--%></td>
                <td><%--${generalMaxScore}--%></td>
                <td><%--${generalClassAverage}--%></td>
            </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
