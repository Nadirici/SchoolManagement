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

<p>
    <a href="${pageContext.request.contextPath}/students/${enrollment.student.id}">Back to Dashboard</a>
</p>

<h1>Course Details</h1>

<div>
    <h2>${enrollment.course.name}</h2>
    <p><strong>Description:</strong> ${enrollment.course.description}</p>
    <p><strong>Teacher:</strong><a href="${pageContext.request.contextPath}/admin/teachers/${enrollment.course.teacher.id}">${enrollment.course.teacher.firstname} ${enrollment.course.teacher.lastname}</a></p>

    <h3>Student Stats:</h3>
    <table border="1">
        <thead>
        <tr>
            <th>Average Grade</th>
            <th>Minimum Grade</th>
            <th>Maximum Grade</th>
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

    <h3>Assignments:</h3>
    <table border="1">
        <thead>
        <tr>
            <th>Assignment Title</th>
            <th>Description</th>
            <th>Student Grade</th>
            <th>Average Grade</th>
            <th>Minimum Grade</th>
            <th>Maximum Grade</th>
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
</body>
</html>
