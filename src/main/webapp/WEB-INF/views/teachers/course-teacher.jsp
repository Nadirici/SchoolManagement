<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Details | Teacher Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Teacher Dashboard</h3>
        <h2>Courses</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/teachers" >Overview</a></li>
            <li><a href="${pageContext.request.contextPath}/teachers/courses" class="active">Courses</a></li>
            <li><a href="${pageContext.request.contextPath}/teachers/assignments">Assignments</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Hi ${teacher.firstname},</h1>
                <h2>Welcome to <span>Courses Dashboard!</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${teacher.firstname} ${teacher.lastname}</span>
                    <span class="user-email">${teacher.email}</span>
                </div>
            </div>
        </header>

        <div class="overviewStudent">
            <h1>Course Details</h1>

            <div>
                <h2>${course.name}</h2>
                <p><strong>Description:</strong> ${course.description}</p>
                <p><strong>Teacher:</strong><a href="${pageContext.request.contextPath}/teachers">${course.teacher.firstname} ${course.teacher.lastname}</a></p>

                <h3>Add New Assignment</h3>
                <form method="post" action="${pageContext.request.contextPath}/teachers">
                    <input type="hidden" name="action" value="createAssignment">
                    <input type="hidden" name="_method" value="POST">
                    <!-- Champ caché pour lier l'Assignment au cours actuel -->
                    <input type="hidden" name="courseId" value="${course.id}"/>

                    <!-- Titre de l'Assignment -->
                    <label for="title">Assignment Title:</label>
                    <input type="text" id="title" name="title" placeholder="Enter assignment title" required/><br/>

                    <!-- Description de l'Assignment -->
                    <label for="assignmentDescription">Description:</label>
                    <textarea id="assignmentDescription" name="description" placeholder="Enter assignment description" required></textarea><br/>

                    <label for="coefficient">Coefficient:</label>
                    <input type="number" id="coefficient" name="coefficient" placeholder="Enter coefficient" step="0.5" required/><br/>

                    <button type="submit">Add Assignment</button>
                </form>


                <h3>Assignments:</h3>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Assignment Title</th>
                        <th>Description</th>
                        <th>Average Grade</th>
                        <th>Minimum Grade</th>
                        <th>Maximum Grade</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="assignment" items="${assignmentStats}">
                        <tr>
                            <td>${assignment.key.title}</td>
                            <td>${assignment.key.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty assignment.value}">
                                        <fmt:formatNumber value="${assignment.value.average}" maxFractionDigits="2"/>
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty assignment.value}">
                                        <fmt:formatNumber value="${assignment.value.min}" maxFractionDigits="2"/>
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty assignment.value}">
                                        <fmt:formatNumber value="${assignment.value.max}" maxFractionDigits="2"/>
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/teachers/assignments/${assignment.key.id}">View Details</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <!-- Ligne pour les statistiques globales -->
                    <tr>
                        <td><strong>Overall Stats</strong></td>
                        <td></td>
                        <td>
                            <fmt:formatNumber value="${courseStats.average}" maxFractionDigits="2"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${courseStats.min}" maxFractionDigits="2"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${courseStats.max}" maxFractionDigits="2"/>
                        </td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>

                <!-- Tableau pour les étudiants inscrits -->
                <h3>Enrolled Students:</h3>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Student Name</th>
                        <th>Average Grade</th>
                        <th>Minimum Grade</th>
                        <th>Maximum Grade</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="enrollment" items="${enrollmentStats}">
                        <tr>
                            <td>${enrollment.key.student.firstname} ${enrollment.key.student.lastname}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty enrollment.value}">
                                        <fmt:formatNumber value="${enrollment.value.average}" maxFractionDigits="2"/>
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty enrollment.value}">
                                        <fmt:formatNumber value="${enrollment.value.min}" maxFractionDigits="2"/>
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty enrollment.value}">
                                        <fmt:formatNumber value="${enrollment.value.max}" maxFractionDigits="2"/>
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
