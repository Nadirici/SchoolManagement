<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Teachers Details</title>
</head>
<body>

<h1>Teachers Details</h1>

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
                        <a href="${pageContext.request.contextPath}/teachers/${teacher.id}/courses/${course.id}">View Details</a>
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
</c:if>

</body>
</html>
