<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Demandes d'inscription</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"> <!-- Lien vers votre fichier CSS -->
</head>
<body>
<div class="container">
<%@include file="../headers/admin_header.jsp" %>
<div class="main-content">
<h1>Demandes d'inscription en attente</h1>

<!-- Affichage d'un message flash si disponible -->
<c:if test="${not empty flashError}">
    <div>${flashError}</div>
</c:if>
    <div class="overviewStudent">
<h2>Demandes des Professeurs</h2>
        <div class="stats">
<table border="1">
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Nom</th>
        <th>Prénom</th>
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
                <td colspan="5">Aucune demande en attente.</td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>
        </div>


<h2>Demandes des Étudiants</h2>
    <div class="stats">
<table border="1">
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Nom</th>
        <th>Prénom</th>
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
                <td colspan="5">Aucune demande en attente.</td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>
    </div>
    </div>

</div>
</body>
</html>