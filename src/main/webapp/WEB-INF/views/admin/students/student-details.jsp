<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Student Details</title>
</head>
<body>

<p>
  <a href="${pageContext.request.contextPath}/admin/students">Back to Student List</a>
</p>

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

</body>
</html>
