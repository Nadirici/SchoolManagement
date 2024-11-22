<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<h1>Course Details</h1>

<div>
    <h2>${course.name}</h2>
    <p><strong>Description:</strong> ${course.description}</p>
    <p><strong>Teacher:</strong><a href="${pageContext.request.contextPath}/teachers/${course.teacher.id}">${course.teacher.firstname} ${course.teacher.lastname}</a></p>

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
                    <a href="${pageContext.request.contextPath}/teachers/${course.teacher.id}/assignments/${assignment.key.id}">View Details</a>
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
</body>
</html>
