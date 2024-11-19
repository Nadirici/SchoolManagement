<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Demandes d'inscription</title>
    <link rel="stylesheet" type="text/css" href="<your-css-file.css>"> <!-- Lien vers votre fichier CSS -->
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}">Back to Dashboard Index</a>
</p>

<h1>Demandes d'inscription en attente</h1>

<!-- Affichage d'un message flash si disponible -->
<c:if test="${not empty flashError}">
    <div>${flashError}</div>
</c:if>

<h2>Demandes des Professeurs</h2>
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
                        <form action="${pageContext.request.contextPath}/requests" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="approve" />
                            <input type="hidden" name="id" value="${teacherRequest.id}" />
                            <button type="submit">Approuver</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/requests" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="reject" />
                            <input type="hidden" name="id" value="${teacherRequest.id}" />
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

<h2>Demandes des Étudiants</h2>
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
                        <form method="post" action="${pageContext.request.contextPath}/requests">
                            <input type="hidden" name="action" value="approve" />
                            <input type="hidden" name="id" value="${studentRequest.id}" />
                            <button type="submit">Approuver</button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/requests">
                            <input type="hidden" name="action" value="reject" />
                            <input type="hidden" name="id" value="${studentRequest.id}" />
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
</body>
</html>