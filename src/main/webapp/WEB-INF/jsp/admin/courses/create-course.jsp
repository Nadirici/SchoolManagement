<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Creation Nouveau cours</title>
  <link rel="stylesheet" href="/css/style.css">
  <script src="/js/course-management.js"></script>

</head>
<body>


<div class="container">
  <%@include file="../../headers/admin_header.jsp" %>
  <div class="main-content">
      <h1>Création d'un nouveau cours</h1>
        <div class="overviewStudent">
          <%
            String flashMessage = (String) request.getAttribute("flashMessage");

            // Vérifiez si le flashMessage est nul et, dans ce cas, récupérez le paramètre de l'URL
            if (flashMessage == null) {
              flashMessage = request.getParameter("flashMessage");
            }
            if (flashMessage != null) {
              switch (flashMessage) {
                case "courseCreated":
                  out.println("<div class='flash-message flash-success'>Cours crée avec succès !</div>");
                  break;
                case "teacherNotfound":
                  out.println("<div class='flash-message flash-error'>Professeur introuvable</div>");
                  break;
                case "existingCourse":
                  out.println("<div class='flash-message flash-error'>Le cours existe déjà</div>");
                  break;
                case"teacherNotAvailable":
                  out.println("<div class='flash-message flash-error'>Le professeur n'est pas disponible pour honoré ces horaires</div>");
                  break;
                default:
                  out.println("<div class='flash-mesage flash-warning'>Erreur inconnue.</div>");
              }
            }
          %>

          <h2>Tous les Cours Disponibles</h2>
          <%@include file="../../lists/courses_list.jsp" %>
          <br>

          <h2>Créer un Cours</h2>
          <div class="form-container">
               <form action="/admin/${admin.id}/courses/create" method="POST">
                    <!-- Département -->
                    <div>
                      <label for="departmentSelect">Département:</label>
                      <select id="departmentSelect" name="department" onchange="loadTeachers(this.value)">
                        <option value="">Selectionner le département</option>
                        <!-- Remplir les départements dynamiquement -->
                        <c:forEach var="department" items="${departments}">
                          <option value="${department}">${department}</option>
                        </c:forEach>
                      </select>
                    </div>

                    <!-- Enseignant -->
                    <div>
                      <label for="teacherSelect">Enseignant:</label>
                      <select id="teacherSelect" name="teacher">
                        <option value="">Selectionner un enseignant</option>
                        <!-- Les enseignants seront ajoutés dynamiquement ici -->
                      </select>
                    </div>

                    <!-- Nom du cours -->
                    <div>
                      <label for="courseName">Nom du Cours </label>
                      <input type="text" id="courseName" name="courseName" required>
                    </div>

                    <!-- Description du cours -->
                    <div>
                      <label for="courseDescription">Course Description:</label>
                      <textarea id="courseDescription" name="courseDescription" required></textarea>
                    </div>


                    <!-- Jour de la semaine -->
                    <div>
                      <label for="dayOfWeek">Jour de la semaine:</label>
                      <select id="dayOfWeek" name="dayOfWeek" required>
                        <option value="">Sélectionner un jour</option>
                        <option value="MONDAY">Lundi</option>
                        <option value="TUESDAY">Mardi</option>
                        <option value="WEDNESDAY">Mercredi</option>
                        <option value="THURSDAY">Jeudi</option>
                        <option value="FRIDAY">Vendredi</option>
                      </select>
                    </div>

                    <!-- Heure de début -->
                    <div>
                      <label for="startTime">Heure de début:</label>
                      <input type="time" id="startTime" name="startTime" required>
                    </div>

                    <!-- Heure de fin -->
                    <div>
                      <label for="endTime">Heure de fin:</label>
                      <input type="time" id="endTime" name="endTime" required>
                    </div>



              <!-- Bouton pour créer le cours -->
              <button id="submitButton" type="submit">Ajouter un Cours</button>
              <div id="admin" data-id="${admin.id}" data-firstname="${admin.firstname}" data-lastname="${admin.lastname}" data-email="${admin.email}"></div>

            </form>
          </div>



    </div>
  </div>
</div>
</body>
</html>
