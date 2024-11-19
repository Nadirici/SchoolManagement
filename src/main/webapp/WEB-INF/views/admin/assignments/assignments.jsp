<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Assignments</title>
</head>
<body>

<p>
  <a href="${pageContext.request.contextPath}">Back to Dashboard Index</a>
</p>

<h1>Assignments</h1>

<h2>Add New Assignment</h2>
<form action="${pageContext.request.contextPath}/assignments" method="post">
  <table>
    <tr>
      <td><label for="courseId">Select Course:</label></td>
      <td>
        <select name="courseId" id="courseId" required>
          <option value="" disabled selected>Select a course</option>
          <c:forEach var="course" items="${availableCourses}">
            <option value="${course.id}">${course.name}</option>
          </c:forEach>
        </select>
      </td>
    </tr>
    <tr>
      <td><label for="title">Title:</label></td>
      <td><input type="text" name="title" id="title" required></td>
    </tr>
    <tr>
      <td><label for="description">Description:</label></td>
      <td><textarea name="description" id="description" required></textarea></td>
    </tr>
    <tr>
      <td><label for="coefficient">Coefficient:</label></td>
      <td><input type="number" name="coefficient" id="coefficient" step="0.01" required></td>
    </tr>
    <tr>
      <td colspan="2">
        <button type="submit">Add Assignment</button>
      </td>
    </tr>
  </table>
</form>

<hr>

<!-- Vérifie si des assignments sont présents -->
<c:if test="${not empty assignments}">
  <table border="1">
    <thead>
    <tr>
      <th>ID</th>
      <th>Assignment Title</th>
      <th>Course</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="assignment" items="${assignments}">
      <tr>
        <td>${assignment.id}</td>
        <td>${assignment.title}</td>
        <td><a href="${pageContext.request.contextPath}/courses?action=view&id=${assignment.course.id}">${assignment.course.name}</a></td>
        <td>
          <a href="${pageContext.request.contextPath}/assignments?action=view&id=${assignment.id}">View Details</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>

<c:if test="${empty assignments}">
  <p>No assignments found.</p>
</c:if>
</body>
</html>
