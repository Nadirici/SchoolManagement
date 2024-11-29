

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste de cours de ${student.firstname}</title>
    <link rel="stylesheet" href="/css/style.css">
     <!-- Lien vers votre fichier CSS -->
</head>
<body>
<div class="container">
<%@include file="../../headers/admin_header.jsp" %>
<div class="main-content">


    <div class="overviewStudent">
    <h2>Liste de cours de  ${student.firstname}</h2>
        <div class="stats">
        <table border="1">
        <thead>
            <tr>
                 <th>Id du cours</th>
                <th>Nom du cours</th>
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
        </div>

    </div>
</div>
</body>
</html>