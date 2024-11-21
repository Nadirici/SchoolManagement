<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Enrollments</title>
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}">Back to Dashboard Index</a>
</p>

<h1>Enrollments List</h1>

<c:if test="${not empty enrollments}">
    <table border="1">
        <thead>
        <tr>
            <th>ID</th>
            <th>Student Name</th>
            <th>Course Name</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="enrollment" items="${enrollments}">
            <tr>
                <td>${enrollment.id}</td>
                <td><a href="${pageContext.request.contextPath}/admin/students?action=view&id=${enrollment.student.id}">${enrollment.student.firstname} ${enrollment.student.lastname}</a></td>
                <td><a href="${pageContext.request.contextPath}/admin/courses?action=view&id=${enrollment.course.id}">${enrollment.course.name}</a></td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/enrollments?action=view&id=${enrollment.id}">View Details</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${empty enrollments}">
    <p>No enrollments found.</p>
</c:if>

</body>
</html>
