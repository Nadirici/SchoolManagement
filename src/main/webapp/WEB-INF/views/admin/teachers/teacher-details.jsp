<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Teacher Details</title>
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}/admin/teachers">Back to Teacher List</a>
</p>

<h1>Teacher Details</h1>

<!-- Vérifie si l'étudiant est présent -->
<c:if test="${not empty teacher}">
    <p><strong>ID:</strong> ${teacher.id}</p>
    <p><strong>First Name:</strong> ${teacher.firstname}</p>
    <p><strong>Last Name:</strong> ${teacher.lastname}</p>
    <p><strong>Department:</strong> ${teacher.department}</p>
    <p><strong>Email:</strong> ${teacher.email}</p>

    <!-- Liste des cours enseignés par ce professeur -->
    <h2>Courses Taught</h2>
    <c:if test="${not empty teacher.courses}">
        <table border="1">
            <thead>
            <tr>
                <th>Course Name</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="course" items="${teacher.courses}">
                <tr>
                    <td>${course.name}</td>
                    <td>${course.description}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/courses?action=view&id=${course.id}">View Details</a>
                        <!-- Optionally add Edit or Delete actions -->
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty teacher.courses}">
        <p>No courses assigned to this teacher.</p>
    </c:if>

    <!-- Formulaire de mise à jour des informations -->
    <h2>Update Teacher</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/teachers">
        <input type="hidden" name="_method" value="PUT" />
        <input type="hidden" name="id" value="${teacher.id}" />

        <label for="firstname">First Name:</label>
        <input type="text" id="firstname" name="firstname" value="${teacher.firstname}" required /><br />

        <label for="lastname">Last Name:</label>
        <input type="text" id="lastname" name="lastname" value="${teacher.lastname}" required /><br />

        <label for="department">Department:</label>
        <input type="text" id="department" name="department" value="${teacher.department}" required /><br />

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${teacher.email}" required /><br />

        <button type="submit">Update</button>
    </form>

    <!-- Formulaire de suppression -->
    <h2>Delete Teacher</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/teachers">
        <input type="hidden" name="_method" value="DELETE" />
        <input type="hidden" name="id" value="${teacher.id}" />
        <button type="submit">Delete</button>
    </form>
</c:if>

<!-- Message si aucun étudiant n'est trouvé -->
<c:if test="${empty teacher}">
    <p>Teacher not found.</p>
</c:if>

</body>
</html>
