<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${assignment.title} | Tableau de bord Ensiegnant</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
  <div class="sidebar">
    <div class="dashboard-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
    </div>
    <h3>Tableau de bord Enseignant</h3>
    <h2>Devoirs</h2>
    <ul>
      <li><a href="${pageContext.request.contextPath}/teachers" >Aperçu</a></li>
      <li><a href="${pageContext.request.contextPath}/teachers/courses" class="active">Cours</a></li>
      <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
    </ul>
  </div>

  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Bonjour ${teacher.firstname},</h1>
        <h2>Bienvenue sur votre <span>Tableau de bord Enseignant !</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username">${teacher.firstname} ${teacher.lastname}</span>
          <span class="user-email">${teacher.email}</span>
        </div>
      </div>
    </header>

    <div class="overviewStudent">
      <h1>Informations sur le devoir</h1>

      <!-- Vérifie si l'assignement est présent -->
      <c:if test="${not empty assignment}">
        <p><strong>Titre :</strong> ${assignment.title}</p>
        <p><strong>Description :</strong> ${assignment.description}</p>
        <p><strong>Coefficient :</strong> ${assignment.coefficient}</p>
        <p><strong>Cours associé :</strong><a href="${pageContext.request.contextPath}/teachers/courses/${assignment.course.id}">${assignment.course.name}</a></p>

        <!-- Formulaire de suppression -->
        <h2>Supprimer le devoir</h2>
        <form method="post" action="${pageContext.request.contextPath}/teachers">
          <input type="hidden" name="_method" value="DELETE" />
          <input type="hidden" name="action" value="deleteAssignment" />
          <input type="hidden" name="id" value="${assignment.id}" />
          <button type="submit">Supprimer</button>
        </form>

        <!-- Liste des étudiants inscrits à cet assignment avec leurs notes -->
        <h2>Liste des étudiants inscrits</h2>
        <c:if test="${not empty enrollmentGrade}">
          <form action="${pageContext.request.contextPath}/teachers" method="post">
            <input type="hidden" name="_method" value="PUT">
            <input type="hidden" name="action" value="saveGrades">
            <input type="hidden" name="courseId" value="${assignment.course.id}">
            <table border="1">
              <thead>
              <tr>
                <th>Étudiant</th>
                <th>Note</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="entry" items="${enrollmentGrade}">
                <tr>
                  <td>${entry.key.student.firstname} ${entry.key.student.lastname}</td> <!-- Student Name -->
                  <td>
                    <input
                            type="number"
                            name="grades[${entry.value.id}]"
                            value="${entry.value.score}"
                            min="0"
                            max="20"
                            step="0.01"
                            required />
                  </td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
            <button type="submit">Save</button>
          </form>
        </c:if>
        <c:if test="${empty enrollmentGrade}">
          <p>Aucun étudiant est inscrit à ce devoir</p>
        </c:if>

        <!-- Statistiques des notes des étudiants -->
        <h3>Statistiques du devoir</h3>
        <table border="1">
          <thead>
          <tr>
            <th>Note moyenne</th>
            <th>Note minimum</th>
            <th>Note maximum</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
              <c:choose>
                <c:when test="${not empty assignmentStats}">
                  <fmt:formatNumber value="${assignmentStats.average}" maxFractionDigits="2"/>
                </c:when>
                <c:otherwise>
                  N/A
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${not empty assignmentStats}">
                  <fmt:formatNumber value="${assignmentStats.min}" maxFractionDigits="2"/>
                </c:when>
                <c:otherwise>
                  N/A
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${not empty assignmentStats}">
                  <fmt:formatNumber value="${assignmentStats.max}" maxFractionDigits="2"/>
                </c:when>
                <c:otherwise>
                  N/A
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          </tbody>
        </table>
      </c:if>

      <c:if test="${empty assignment}">
        <p>Ce devoir n'a pas été trouvé.</p>
      </c:if>
    </div>
  </div>
</div>
</body>
</html>
