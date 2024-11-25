<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Requests | Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Admin Dashboard</h3>
        <h2>Requests</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin">Overview</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/students">Students</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/teachers">Teachers</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/courses" >Courses</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/requests" class="active">Requests</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Hi ${admin.firstname},</h1>
                <h2>Welcome to <span>Requests Dashboard!</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${admin.firstname} ${admin.lastname}</span>
                    <span class="user-email">${admin.email}</span>
                </div>
            </div>
        </header>

        <div class="overviewStudent">
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
                                    <form action="${pageContext.request.contextPath}/admin/requests" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="approve" />
                                        <input type="hidden" name="id" value="${teacherRequest.id}" />
                                        <button type="submit">Approuver</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/requests" method="post" style="display:inline;">
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
                                    <form method="post" action="${pageContext.request.contextPath}/admin/requests">
                                        <input type="hidden" name="action" value="approve" />
                                        <input type="hidden" name="id" value="${studentRequest.id}" />
                                        <button type="submit">Approuver</button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/requests">
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
        </div>
    </div>
</div>
</body>
</html>