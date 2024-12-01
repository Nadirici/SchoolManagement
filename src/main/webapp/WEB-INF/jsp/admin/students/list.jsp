
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="/css/style.css">
  <title>Liste d'étudiants</title>
</head>
<body>
<div class="container">
  <%@include file="../../headers/admin_header.jsp" %>
  <div class="main-content">
    <h1>Liste d'étudiants</h1>

    <div class="overviewStudent">
        <!-- Barre de recherche -->
        <input type="text" id="searchInput" placeholder="Rechercher..." onkeyup="filterTable()">
      <table border="1" id="myTable">
      <thead>
        <tr>
        <th>ID</th>
        <th>Prénom</th>
        <th>Nom</th>
        <th>Email</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.id}</td>
            <td>${student.firstname}</td>
            <td>${student.lastname}</td>
            <td>${student.email}</td>
            <td>
                <a href="students/${student.id}">Information sur l'étudiant</a> |
                <a href="students/${student.id}/courses">Ses Cours</a> |
                <a href="students/${student.id}/delete">Supprimer</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


<script src="/js/search.js"></script>
</body>
</html>
