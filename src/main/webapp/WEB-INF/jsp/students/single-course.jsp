
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>${course.name}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>








<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--! Font Awesome Free --><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Student Dashboard</h3>
        <ul>
            <li><a href="/students/${student.id}" class="active">Dashboard</a></li>
            <li><a href="/students/${student.id}/enroll">S'inscrire au cours</a></li>

            <li><a href="/students/${student.id}/profile">Profile</a></li>
            <form action="/logout" method="post">
                <li><input type="submit" value="Logout"></li>
            </form>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Bonjour ${student.firstname},</h1>
                <h2>Bienvenue sur ton <span>Dashboard!</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${student.firstname} ${student.lastname}</span>
                    <span class="user-email">${student.email}</span>
                </div>
            </div>
        </header>

        <div class="overviewStudent">
            <h2>${course.name}</h2>
            <div class="stats">
                <div class="stat-card">
                    <h3>Description :</h3>
                    <p> ${course.description}</p>
                </div>
                <div class="stat-card">
                    <h3>Enseignant :</h3>
                    <p>${course.teacher.firstname} ${course.teacher.lastname}</p>
                </div>
            </div>



            <h2>Devoirs</h2>
            <c:if test="${canViewDetails}">
                <c:choose>
                    <c:when test="${empty assignments}">
                        <p>Il n'y a pas encore eu d'évaluation.</p>
                    </c:when>
                    <c:otherwise>
                        <table border="1">
                            <thead>
                            <tr>
                                <th>Nom de l'évaluation</th>
                                <th>Coefficient</th>
                                <th>Note minimale</th>
                                <th>Note maximale</th>
                                <th>Moyenne de la classe</th>
                                <th>Note de l'étudiant</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="assignment" items="${assignments}">
                                <tr>
                                    <td>${assignment.title}</td>
                                    <td>${assignment.coefficient}</td>
                                    <td><c:out value="${minGrade[assignment.id]}" /></td>
                                    <td><c:out value="${maxGrade[assignment.id]}" /></td>
                                    <td><c:out value="${averageGrades[assignment.id]}" /></td>
                                    <c:choose>
                                        <c:when test="${isEnrolledStudent}">
                                            <!-- Afficher la note de l'étudiant ou un message si la note est absente -->
                                            <td>
                                                <c:if test="${empty studentAssignmentGrades[assignment.id]}">
                                                    Tu n'as pas de note
                                                </c:if>
                                                <c:if test="${not empty studentAssignmentGrades[assignment.id]}">
                                                    <c:out value="${studentAssignmentGrades[assignment.id]}" />
                                                </c:if>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>Non disponible</td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
                <c:if test="${!isEnrolledStudent}">
                    <a href="../courses/${course.id}/grades">Voir les notes</a>
                </c:if>
                <c:if test="${isAssignedTeacher}">
                    <button onclick="window.location.href='/courses/${course.id}/add-assignment'">+ Ajouter un devoir</button>
                </c:if>
            </c:if>

        </div>
    </div>
</div>
</body>
</html>
