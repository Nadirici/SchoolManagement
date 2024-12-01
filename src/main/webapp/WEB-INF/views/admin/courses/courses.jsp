<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Liste des cours | Tableau de bord Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
  <div class="sidebar">
    <div class="dashboard-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
    </div>
    <h3>Tableau de bord Admin</h3>
    <h2>Cours</h2>
    <ul>
      <li><a href="${pageContext.request.contextPath}/admin">Aperçu</a></li>
      <li><a href="${pageContext.request.contextPath}/admin/students">Étudiants</a></li>
      <li><a href="${pageContext.request.contextPath}/admin/teachers">Enseignants</a></li>
      <li><a href="${pageContext.request.contextPath}/admin/courses" class="active">Cours</a></li>
      <li><a href="${pageContext.request.contextPath}/admin/requests">Demandes d'inscription</a></li>
      <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
    </ul>
  </div>

  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Bonjour ${admin.firstname},</h1>
        <h2>Bienvenue sur votre <span>Tableau de bord Admin !</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username">${admin.firstname} ${admin.lastname}</span>
          <span class="user-email">${admin.email}</span>
        </div>
      </div>
    </header>

    <div class="overviewStudent">
      <h1>Gestion des cours</h1>


      <h3>Ajouter un nouveau cours</h3>

      <!-- Affichage du message flash si disponible -->
      <% String flashMessage = request.getParameter("flashMessage"); %>
      <% if (flashMessage != null && flashMessage.equals("notAvailable")) { %>
      <div class="flash-message">
        <p>Le professeur n'est pas disponible pour ce cours.</p>
      </div>
      <% } %>

      <form method="post" action="${pageContext.request.contextPath}/admin/courses">
        <label for="name">Nom :</label>
        <input type="text" id="name" name="name" required /><br />

        <label for="description">Description :</label>
        <input type="text" id="description" name="description" required /><br />

        <label for="teacherId">Enseignant :</label>
        <select id="teacherId" name="teacherId" required>
          <option value="">Choisir un enseignant</option>
          <c:forEach var="teacher" items="${availableTeachers}">
            <option value="${teacher.id}">
                ${teacher.firstname} ${teacher.lastname}
            </option>
          </c:forEach>
        </select><br/>

        <label for="dayOfWeek">Jour :</label>
        <select id="dayOfWeek" name="dayOfWeek" required>
          <option value="">Choisir un jour</option>
          <option value="MONDAY">Lundi</option>
          <option value="TUESDAY">Mardi</option>
          <option value="WEDNESDAY">Mercredi</option>
          <option value="THURSDAY">Jeudi</option>
          <option value="FRIDAY">Vendredi</option>
        </select><br/>

        <label for="startTime">Heure de début :</label>
        <input type="time" id="startTime" name="startTime" required /><br />

        <label for="endTime">Heure de fin :</label>
        <input type="time" id="endTime" name="endTime" required /><br />

        <button type="submit">Ajouter</button>
      </form>
    </div>
    <div class="overviewStudent">
      <h2>Cours actuels</h2>
      <!-- Barre de recherche -->
      <input type="text" id="searchInput" placeholder="Rechercher..." onkeyup="filterTable()">
      <table border="1" id="myTable">
        <thead>
        <tr>
          <th>ID</th>
          <th>Nom du cours</th>
          <th>Description</th>
          <th>Enseignant</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="course" items="${courses}">
          <tr>
            <td>${course.id}</td>
            <td>${course.name}</td>
            <td>${course.description}</td>
            <td><a href="${pageContext.request.contextPath}/admin/teachers/${course.teacher.id}">${course.teacher.firstname} ${course.teacher.lastname}</a></td>
            <td>
              <div class="action-container">
              <a href="${pageContext.request.contextPath}/admin/courses/${course.id}">Détails</a>
              <form method="post" action="${pageContext.request.contextPath}/admin/courses">
                <input type="hidden" name="_method" value="DELETE" />
                <input type="hidden" name="id" value="${course.id}" />
                <button type="submit" class="table-button">| Supprimer</button>
              </form>
              </div>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>

    </div>
  </div>
</div>
<script src="${pageContext.request.contextPath}/js/search.js"></script>
</body>
</html>
