<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Registration Requests</title>
    <link rel="stylesheet" href="/css/admin/requests.css">
</head>
<body>
<h1>Registration Requests in Progress</h1>

<!-- Affichage d'un message flash si disponible -->
<c:if test="${not empty flashError}">
    <div>${flashError}</div>
</c:if>

<h2>Teacher Requests</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Last Name</th>
        <th>First Name</th>
        <th>Actions</th>
    </tr>
    <c:choose>
        <c:when test="${not empty pendingTeacherRequests}">
            <c:forEach var="teacherRequest" items="${pendingTeacherRequests}">
                <tr>
                    <td>${teacherRequest.id}</td>
                    <td>${teacherRequest.teacher.email}</td>
                    <td>${teacherRequest.teacher.lastname}</td>
                    <td>${teacherRequest.teacher.firstname}</td>
                    <td>
                        <form action="/admin/${adminId}/requests/${teacherRequest.id}/approve" method="post" style="display:inline;">
                            <button type="submit">Approuver</button>
                        </form>
                        <form action="/admin/${adminId}/requests/${teacherRequest.id}/reject" method="post" style="display:inline;">
                            <button type="submit">Rejeter</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="5">No Requests in Progress.</td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>

<h2>Student Requests</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Last Name</th>
        <th>First Name</th>
        <th>Actions</th>
    </tr>
    <c:choose>
        <c:when test="${not empty pendingStudentRequests}">
            <c:forEach var="studentRequest" items="${pendingStudentRequests}">
                <tr>
                    <td>${studentRequest.id}</td>
                    <td>${studentRequest.student.email}</td>
                    <td>${studentRequest.student.lastname}</td>
                    <td>${studentRequest.student.firstname}</td>
                    <td>
                        <form action="/admin/${adminId}/requests/${studentRequest.id}/approve" method="post" style="display:inline;">
                            <button type="submit">Approuver</button>
                        </form>
                        <form action="/admin/${adminId}/requests/${studentRequest.id}/approve" method="post" style="display:inline;">
                            <button type="submit">Rejeter</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="5">No Requests in Progress.</td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>
</body>
</html>