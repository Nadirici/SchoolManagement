<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Course Details</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

  <%@ include file="../headers/admin_header.jsp" %> <!-- Assurez-vous que le fichier existe -->

  <div class="main-content">
    <div class="overviewStudent">
      <h2>${course.name}</h2>

      <!-- Formulaire de mise à jour du cours -->
      <form method="post" action="${pageContext.request.contextPath}/admin/${admin.id}/courses/update/${course.id}">
        <!-- Champ caché pour inclure l'ID du cours -->
        <input type="hidden" name="id" value="${course.id}" />

        <!-- Nom du cours -->
        <label for="name">Nom du cours :</label>
        <input type="text" id="name" name="name" value="${course.name}" required /><br/>

        <!-- Description du cours -->
        <label for="courseDescription">Description :</label>
        <textarea id="courseDescription" name="description" required>${course.description}</textarea><br/>

        <!-- Enseignant affecté -->
        <label for="teacherId">Enseignant :</label>
        <select id="teacherId" name="teacherId" required>
            <option value="${course.teacher.id}">${course.teacher.firstname} ${course.teacher.lastname}</option>
            <!-- Remplir dynamiquement les enseignants en fonction du département de l'enseignant actuel -->
            <c:forEach var="teacher" items="${availableTeachers}">
                <option value="${teacher.id}" <c:if test="${teacher.id == course.teacher.id}">selected</c:if>>
                    ${teacher.firstname} ${teacher.lastname}
                </option>
            </c:forEach>
        </select><br/>

        <!-- Jour d'enseignement -->
        <label for="day">Jour :</label>
        <select id="day" name="dayOfWeek" required>
            <option value="">Sélectionner un jour</option>
            <option value="MONDAY" <c:if test="${course.dayOfWeek == 'MONDAY'}">selected</c:if>>Lundi</option>
            <option value="TUESDAY" <c:if test="${course.dayOfWeek == 'TUESDAY'}">selected</c:if>>Mardi</option>
            <option value="WEDNESDAY" <c:if test="${course.dayOfWeek == 'WEDNESDAY'}">selected</c:if>>Mercredi</option>
            <option value="THURSDAY" <c:if test="${course.dayOfWeek == 'THURSDAY'}">selected</c:if>>Jeudi</option>
            <option value="FRIDAY" <c:if test="${course.dayOfWeek == 'FRIDAY'}">selected</c:if>>Vendredi</option>
        </select><br/>

        <!-- Heure de début -->
        <label for="startTime">Heure de début :</label>
        <input type="time" id="startTime" name="startTime" value="${course.startTime}" required /><br/>

        <!-- Heure de fin -->
        <label for="endTime">Heure de fin :</label>
        <input type="time" id="endTime" name="endTime" value="${course.endTime}" required /><br/>

        <!-- Bouton de soumission -->
        <button type="submit">Sauvegarder les modifications</button>
      </form>

      <!-- Statistiques supplémentaires -->
      <div class="stats">
        <div class="stat-card">
          <h3>Description :</h3>
          <p>${course.description}</p>
        </div>
        <div class="stat-card">
          <h3>Enseignant :</h3>
          <p>${course.teacher.firstname} ${course.teacher.lastname}</p>
        </div>
      </div>

      <%@ include file="../courses/course_details_table.jsp" %> <!-- Vérifiez que le fichier existe -->

    </div> <!-- Fermeture de .overviewStudent -->
  </div> <!-- Fermeture de .main-content -->
</div> <!-- Fermeture de .container -->

</body>
</html>
