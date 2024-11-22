<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Course Details</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<p>
  <a href="${pageContext.request.contextPath}/admin/courses">Back to Courses List</a>
</p>

<h1>Course Details</h1>

<div>
  <h2>${course.name}</h2>
  <p><strong>Description:</strong> ${course.description}</p>
  <p><strong>Teacher:</strong><a href="${pageContext.request.contextPath}/admin/teachers/${course.teacher.id}">${course.teacher.firstname} ${course.teacher.lastname}</a></p>

  <h3>Edit Course</h3>
  <form method="post" action="${pageContext.request.contextPath}/admin/courses">
    <input type="hidden" name="_method" value="PUT" />
    <input type="hidden" name="id" value="${course.id}"/>
    <label for="name">Course Name:</label>
    <input type="text" id="name" name="name" value="${course.name}" required/><br/>

    <label for="courseDescription">Description:</label>
    <textarea id="courseDescription" name="description" required>${course.description}</textarea><br/>

      <label for="teacherId">Teacher:</label>
      <select id="teacherId" name="teacherId" required>
          <option value="">Select a teacher</option>
          <c:forEach var="teacher" items="${availableTeachers}">
              <option value="${teacher.id}"
                      <c:if test="${teacher.id == course.teacher.id}">selected</c:if>>
                      ${teacher.firstname} ${teacher.lastname}
              </option>
          </c:forEach>
      </select><br/>

    <button type="submit">Save Changes</button>
  </form>

  <h3>Add New Assignment</h3>
  <form method="post" action="${pageContext.request.contextPath}/admin/assignments">
    <input type="hidden" name="_method" value="POST">
    <!-- Champ caché pour lier l'Assignment au cours actuel -->
    <input type="hidden" name="courseId" value="${course.id}"/>

    <!-- Titre de l'Assignment -->
    <label for="title">Assignment Title:</label>
    <input type="text" id="title" name="title" placeholder="Enter assignment title" required/><br/>

    <!-- Description de l'Assignment -->
    <label for="assignmentDescription">Description:</label>
    <textarea id="assignmentDescription" name="description" placeholder="Enter assignment description" required></textarea><br/>

    <label for="coefficient">Coefficient:</label>
    <input type="number" id="coefficient" name="coefficient" placeholder="Enter coefficient" step="0.5" required/><br/>

    <button type="submit">Add Assignment</button>
  </form>


  <h3>Assignments:</h3>
  <table border="1">
    <thead>
    <tr>
      <th>Assignment Title</th>
      <th>Description</th>
      <th>Average Grade</th>
      <th>Minimum Grade</th>
      <th>Maximum Grade</th>
      <th>Actions</th>
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
        <td>
          <a href="${pageContext.request.contextPath}/admin/assignments/${assignment.key.id}">View Details</a>
        </td>
      </tr>
    </c:forEach>
      <!-- Ligne pour les statistiques globales -->
      <tr>
        <td><strong>Overall Stats</strong></td>
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
        <td></td>
      </tr>
    </tbody>
  </table>

  <!-- Tableau pour les étudiants inscrits -->
  <h3>Enrolled Students:</h3>
  <table border="1">
    <thead>
    <tr>
      <th>Student Name</th>
      <th>Average Grade</th>
      <th>Minimum Grade</th>
      <th>Maximum Grade</th>
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
          <a href="${pageContext.request.contextPath}/admin/enrollments/${enrollment.key.id}">View Enrollment</a><br/>
          <form method="post" action="${pageContext.request.contextPath}/admin/enrollments">
            <input type="hidden" name="_method" value="DELETE"/>
            <input type="hidden" name="id" value="${enrollment.key.id}"/>
            <button type="submit">Remove</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <form method="post" action="${pageContext.request.contextPath}/admin/enrollments">
    <input type="hidden" name="_method" value="POST">
    <input type="hidden" name="courseId" value="${course.id}"/>
    <label for="studentId">Add Student:</label>
    <select id="studentId" name="studentId" required>
      <option value="">Select a student</option>
      <c:forEach var="student" items="${availableStudents}">
        <option value="${student.id}">${student.firstname} ${student.lastname}</option>
      </c:forEach>
    </select>
    <button type="submit">Add</button>
  </form>

  <!-- Formulaire de suppression -->
  <h2>Delete Course</h2>
  <form method="post" action="${pageContext.request.contextPath}/admin/courses">
    <input type="hidden" name="_method" value="DELETE" />
    <input type="hidden" name="id" value="${course.id}" />
    <button type="submit">Delete Course</button>
  </form>

</div>
</body>
</html>
