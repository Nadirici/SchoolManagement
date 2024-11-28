<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${enrollment.course.name} | Tableau de bord Étudiant</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Tableau de bord Étudiant</h3>
        <h2>Cours</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/students" >Aperçu</a></li>
            <li><a href="${pageContext.request.contextPath}/students/courses" class="active">Cours</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Bonjour ${enrollment.student.firstname},</h1>
                <h2>Bienvenue sur votre <span>Tableau de bord Étudiant</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${enrollment.student.firstname} ${enrollment.student.lastname}</span>
                    <span class="user-email">${enrollment.student.email}</span>
                </div>
            </div>
        </header>

        <div class="overviewStudent">
            <h1>Informations sur le cours</h1>

            <div>
                <h2>${enrollment.course.name}</h2>
                <p><strong>Description :</strong> ${enrollment.course.description}</p>
                <p><strong>Enseignant :</strong> ${enrollment.course.teacher.firstname} ${enrollment.course.teacher.lastname}</p>

                <h3>Statistiques générales :</h3>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Note moyenne</th>
                        <th>Note minimum</th>
                        <th>Note maximum</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${not empty enrollmentStats}">
                                    <fmt:formatNumber value="${enrollmentStats.average}" maxFractionDigits="2" />
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty enrollmentStats}">
                                    <fmt:formatNumber value="${enrollmentStats.min}" maxFractionDigits="2" />
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty enrollmentStats}">
                                    <fmt:formatNumber value="${enrollmentStats.max}" maxFractionDigits="2" />
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <h3>Devoirs :</h3>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Titre</th>
                        <th>Description</th>
                        <th>Ma note</th>
                        <th>Note moyenne</th>
                        <th>Note minimum</th>
                        <th>Note maximum</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="entry" items="${assignmentsData}">
                        <tr>
                            <td>${entry.key.title}</td>
                            <td>${entry.key.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty entry.value.grade}">
                                        <fmt:formatNumber value="${entry.value.grade.score}" maxFractionDigits="2" />
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty entry.value.stats}">
                                        <fmt:formatNumber value="${entry.value.stats.average}" maxFractionDigits="2" />
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty entry.value.stats}">
                                        <fmt:formatNumber value="${entry.value.stats.min}" maxFractionDigits="2" />
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty entry.value.stats}">
                                        <fmt:formatNumber value="${entry.value.stats.max}" maxFractionDigits="2" />
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
