<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${course.name} | Tableau de bord Admin</title>
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
      <h1>Détails du cours</h1>

      <div>
        <h2>${course.name}</h2>
        <p><strong>Description :</strong> ${course.description}</p>
        <p><strong>Enseignant :</strong> <a href="${pageContext.request.contextPath}/admin/teachers/${course.teacher.id}">${course.teacher.firstname} ${course.teacher.lastname}</a></p>

        <h3>Devoirs :</h3>
        <table border="1">
          <thead>
          <tr>
            <th>Titre</th>
            <th>Description</th>
            <th>Note moyenne</th>
            <th>Note minimum</th>
            <th>Note maximum</th>
            <%--<th>Actions</th>--%>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="assignment" items="${assignmentStats}">
            <tr>
              <td>${assignment.key.title}</td>
              <td>${assignment.key.description}</td>
              <td>
                <c:choose>
                  <c:when test="${not empty assignment.value}">
                    <fmt:formatNumber value="${assignment.value.average}" maxFractionDigits="2"/>
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty assignment.value}">
                    <fmt:formatNumber value="${assignment.value.min}" maxFractionDigits="2"/>
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty assignment.value}">
                    <fmt:formatNumber value="${assignment.value.max}" maxFractionDigits="2"/>
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
<%--              <td>
                <a href="${pageContext.request.contextPath}/admin/assignments/${assignment.key.id}">View Details</a>
              </td>--%>
            </tr>
          </c:forEach>
          <!-- Ligne pour les statistiques globales -->
          <tr>
            <td><strong>Statistiques générales</strong></td>
            <td></td>
            <td>
              <fmt:formatNumber value="${courseStats.average}" maxFractionDigits="2"/>
            </td>
            <td>
              <fmt:formatNumber value="${courseStats.min}" maxFractionDigits="2"/>
            </td>
            <td>
              <fmt:formatNumber value="${courseStats.max}" maxFractionDigits="2"/>
            </td>
            <%--<td></td>--%>
          </tr>
          </tbody>
        </table>

        <!-- Tableau pour les étudiants inscrits -->
        <h3>Étudiants inscrit :</h3>
        <table border="1">
          <thead>
          <tr>
            <th>Étudiant</th>
            <th>Note moyenne</th>
            <th>Note minimum</th>
            <th>Note maximum</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="enrollment" items="${enrollmentStats}">
            <tr>
              <td>${enrollment.key.student.firstname} ${enrollment.key.student.lastname}</td>
              <td>
                <c:choose>
                  <c:when test="${not empty enrollment.value}">
                    <fmt:formatNumber value="${enrollment.value.average}" maxFractionDigits="2"/>
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty enrollment.value}">
                    <fmt:formatNumber value="${enrollment.value.min}" maxFractionDigits="2"/>
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty enrollment.value}">
                    <fmt:formatNumber value="${enrollment.value.max}" maxFractionDigits="2"/>
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <form class="form-button" method="post" action="${pageContext.request.contextPath}/admin/enrollments">
                  <input type="hidden" name="_method" value="DELETE"/>
                  <input type="hidden" name="id" value="${enrollment.key.id}"/>
                  <button type="submit">Désinscrire</button>
                </form>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <div class="form-container">
        <div class="form-column">
          <h3>Modifier le cours</h3>

          <% String flashMessage = request.getParameter("flashMessage"); %>
          <% if (flashMessage != null && flashMessage.equals("teacherNotAvailable")) { %>

          <div class="flash-message flash-error">
            <p>Le professeur n'est pas disponible pour ce cours, vous ne pouvez donc pas modifier le cours</p>
          </div>

          <% } else if (flashMessage != null && flashMessage.equals("studentNotAvailable")) { %>
          <div class="flash-message flash-error">
            <p>Des étudiants ne sont pas disponible, vous ne pouvez donc pas modifier le cours.</p>
          </div>
          <%
          } else if (flashMessage != null && flashMessage.equals("singleNotAvailable")) { %>
          <div class="flash-message flash-error">
            <p>L'étudiant n'est pas disponible pour être ajouté à ce cours. </p>
          </div>
          <% } %>


          <form method="post" action="${pageContext.request.contextPath}/admin/courses">
            <input type="hidden" name="_method" value="PUT" />
            <input type="hidden" name="id" value="${course.id}"/>
            <label for="name">Nom :</label>
            <input type="text" id="name" name="name" value="${course.name}" required/><br/>

            <label for="courseDescription">Description:</label>
            <textarea id="courseDescription" name="description" required>${course.description}</textarea><br/>


            <label for="teacherId">Enseignant :</label>
            <select id="teacherId" name="teacherId" required>

              <option value="">Choisir un enseignant</option>
              <c:forEach var="teacher" items="${availableTeachers}">
                <option value="${teacher.id}"
                        <c:if test="${teacher.id == course.teacher.id}">selected</c:if>>
                    ${teacher.firstname} ${teacher.lastname}
                </option>

              </c:forEach>

            </select>
            <br/>



            <label for="day">Jour :</label>
            <select id="day" name="day" required>
              <option value="">Choisir un jour</option>
              <option value="MONDAY" <c:if test="${course.dayOfWeek == 'MONDAY'}">selected</c:if>>Lundi</option>
              <option value="TUESDAY" <c:if test="${course.dayOfWeek == 'TUESDAY'}">selected</c:if>>Mardi</option>
              <option value="WEDNESDAY" <c:if test="${course.dayOfWeek == 'WEDNESDAY'}">selected</c:if>>Mercredi</option>
              <option value="THURSDAY" <c:if test="${course.dayOfWeek == 'THURSDAY'}">selected</c:if>>Jeudi</option>
              <option value="FRIDAY" <c:if test="${course.dayOfWeek == 'FRIDAY'}">selected</c:if>>Vendredi</option>
            </select><br/>

            <label for="startTime">Heure de début :</label>
            <input type="time" id="startTime" name="startTime" value="${course.startTime}" required/><br/>

            <label for="endTime">Heure de fin :</label>
            <input type="time" id="endTime" name="endTime" value="${course.endTime}" required/><br/>


            <button type="submit">Modifier</button>
          </form>
        </div>

        <div class="form-column">
          <h3>Inscrire un étudiant</h3>
          <form method="post" action="${pageContext.request.contextPath}/admin/enrollments">
            <input type="hidden" name="_method" value="POST">
            <input type="hidden" name="courseId" value="${course.id}"/>
            <select id="studentId" name="studentId" required>
              <option value="">Choisir un étudiant</option>
              <c:forEach var="student" items="${availableStudents}">
                <option value="${student.id}">${student.firstname} ${student.lastname}</option>
              </c:forEach>
            </select>
            <button type="submit">Inscrire</button>
          </form>
        </div>
        <div class="form-column">
          <!-- Formulaire de suppression -->
          <h3>Supprimer le cours</h3>
          <form method="post" action="${pageContext.request.contextPath}/admin/courses">
            <input type="hidden" name="_method" value="DELETE" />
            <input type="hidden" name="id" value="${course.id}" />
            <button type="submit">Supprimer</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
