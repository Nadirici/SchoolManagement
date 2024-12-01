<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="/css/style.css">
  <title>Liste d'enseignants</title>
</head>
<body>
<div class="container">
  <%@include file="../../headers/admin_header.jsp" %>
  <div class="main-content">
    <h1>Liste d'enseignants</h1>

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
         <c:forEach var="teacher" items="${teachers}">
      <tr>
      <td>${teacher.id}</td>
      <td>${teacher.firstname}</td>
      <td>${teacher.lastname}</td>
      <td>${teacher.email}</td>
      <td>



        <a href="teachers/${teacher.id}">Information sur l'enseignant |</a>
        <a href="teachers/${teacher.id}/courses">Cours enseigné</a> |
         <a href="teachers/${teacher.id}/delete">Supprimer</a>
      </td>
    </tr>
  </c:forEach>
      </table>

    </div>

  </div>
</div>
<script src="/js/search.js"></script>
</body>
</html>
