<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Course Management</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<p>
  <a href="${pageContext.request.contextPath}">Back to Dashboard Index</a>
</p>

<h1>Course List</h1>

<!-- Form to add a new course -->
<h2>Add a New Course</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/courses">
  <label for="name">Name:</label>
  <input type="text" id="name" name="name" required /><br />

  <label for="description">Description:</label>
  <input type="text" id="description" name="description" required /><br />

  <label for="teacherId">Teacher:</label>
  <select id="teacherId" name="teacherId" required>
    <option value="">Select a teacher</option>
    <c:forEach var="teacher" items="${availableTeachers}">
      <option value="${teacher.id}">
          ${teacher.firstname} ${teacher.lastname}
      </option>
    </c:forEach>
  </select><br/>

  <button type="submit">Add Course</button>
</form>

<hr />

<table border="1">
  <thead>
  <tr>
    <th>Course Id</th>
    <th>Course Name</th>
    <th>Description</th>
    <th>Teacher</th>
    <th>Actions</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="course" items="${courses}">
    <tr>
      <td>${course.id}</td>
      <td>${course.name}</td>
      <td>${course.description}</td>
      <td><a href="${pageContext.request.contextPath}/admin/teachers?action=view&id=${course.teacher.id}">${course.teacher.firstname} ${course.teacher.lastname}</a></td>
      <td>
        <a href="${pageContext.request.contextPath}/admin/courses?action=view&id=${course.id}">View Details</a>
        <!-- Optionally add Edit or Delete actions -->
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>
