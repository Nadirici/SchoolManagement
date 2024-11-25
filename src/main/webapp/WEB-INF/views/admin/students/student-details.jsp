<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Student Details | Admin Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <div class="container">
    <div class="sidebar">
      <div class="dashboard-icon">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
      </div>
      <h3>Admin Dashboard</h3>
      <h2>Students</h2>
      <ul>
        <li><a href="${pageContext.request.contextPath}/admin">Overview</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/students" class="active">Students</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/teachers">Teachers</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/courses" >Courses</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/requests">Demandes d'inscription</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
      </ul>
    </div>
  </div>
  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Hi ${admin.firstname},</h1>
        <h2>Welcome to <span>Students Dashboard!</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username">${admin.firstname} ${admin.lastname}</span>
          <span class="user-email">${admin.email}</span>
        </div>
      </div>
    </header>
    <div class="overviewStudent">
      <h1>Student Details</h1>

      <!-- Vérifie si l'étudiant est présent -->
      <c:if test="${not empty student}">
        <p><strong>ID:</strong> ${student.id}</p>
        <p><strong>First Name:</strong> ${student.firstname}</p>
        <p><strong>Last Name:</strong> ${student.lastname}</p>
        <p><strong>Date of Birth:</strong> ${student.dateOfBirth}</p>
        <p><strong>Email:</strong> ${student.email}</p>

        <h3>Enrollments:</h3>
        <table border="1">
          <thead>
          <tr>
            <th>Course Name</th>
            <th>Average Grade</th>
            <th>Minimum Grade</th>
            <th>Maximum Grade</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="enrollment" items="${enrollmentStats}">
            <tr>
              <!-- Affiche les détails du cours -->
              <td>${enrollment.key.course.name}</td>

              <!-- Affiche les statistiques -->
              <td>
                <c:choose>
                  <c:when test="${not empty enrollment.value}">
                    <fmt:formatNumber value="${enrollment.value.average}" maxFractionDigits="2" />
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty enrollment.value}">
                    <fmt:formatNumber value="${enrollment.value.min}" maxFractionDigits="2" />
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty enrollment.value}">
                    <fmt:formatNumber value="${enrollment.value.max}" maxFractionDigits="2" />
                  </c:when>
                  <c:otherwise>
                    N/A
                  </c:otherwise>
                </c:choose>
              </td>

              <!-- Actions -->
              <td>
                <a href="${pageContext.request.contextPath}/admin/courses/${enrollment.key.course.id}">
                  View Course
                </a>
              </td>
            </tr>
          </c:forEach>
          <!-- Ligne pour les statistiques globales -->
          <tr>
            <td><strong>Overall Stats</strong></td>
            <td>
              <fmt:formatNumber value="${studentStats.average}" maxFractionDigits="2"/>
            </td>
            <td>
              <fmt:formatNumber value="${studentStats.min}" maxFractionDigits="2"/>
            </td>
            <td>
              <fmt:formatNumber value="${studentStats.max}" maxFractionDigits="2"/>
            </td>
            <td></td>
          </tr>
          </tbody>
        </table>

        <!-- Liste déroulante pour ajouter un nouveau cours -->
        <h2>Enroll in a New Course</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/enrollments">
          <input type="hidden" name="_method" value="POST">
          <input type="hidden" name="studentId" value="${student.id}" />

          <label for="courseId">Available Courses:</label>
          <select id="courseId" name="courseId" required>
            <option value="" disabled selected>Select a course</option>
            <c:forEach var="course" items="${availableCourses}">
              <option value="${course.id}">${course.name} - ${course.description}</option>
            </c:forEach>
          </select>

          <button type="submit">Enroll</button>
        </form>

        <!-- Formulaire de mise à jour des informations -->
        <h2>Update Student</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/students">
          <input type="hidden" name="_method" value="PUT" />
          <input type="hidden" name="id" value="${student.id}" />

          <label for="firstname">First Name:</label>
          <input type="text" id="firstname" name="firstname" value="${student.firstname}" required /><br />

          <label for="lastname">Last Name:</label>
          <input type="text" id="lastname" name="lastname" value="${student.lastname}" required /><br />

          <label for="dateOfBirth">Date of Birth:</label>
          <input type="date" id="dateOfBirth" name="dateOfBirth" value="${student.dateOfBirth}" required /><br />

          <label for="email">Email:</label>
          <input type="email" id="email" name="email" value="${student.email}" required /><br />

          <button type="submit">Update</button>
        </form>

        <!-- Formulaire de suppression -->
        <h2>Delete Student</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/students">
          <input type="hidden" name="_method" value="DELETE" />
          <input type="hidden" name="id" value="${student.id}" />
          <button type="submit">Delete</button>
        </form>
      </c:if>

      <!-- Message si aucun étudiant n'est trouvé -->
      <c:if test="${empty student}">
        <p>Student not found.</p>
      </c:if>
    </div>
  </div>
</body>
</html>
