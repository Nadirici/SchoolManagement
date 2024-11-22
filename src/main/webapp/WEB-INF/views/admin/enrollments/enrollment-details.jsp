<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Enrollment Details</title>
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}/admin/enrollments">Back to Enrollments List</a>
</p>

<h1>Enrollment Details</h1>

<c:if test="${not empty enrollment}">
    <p><strong>Student:</strong><a href="${pageContext.request.contextPath}/admin/students/${enrollment.student.id}">${enrollment.student.firstname} ${enrollment.student.lastname}</a></p>
    <p><strong>Course:</strong>
        <a href="${pageContext.request.contextPath}/admin/courses/${enrollment.course.id}">
                ${enrollment.course.name}
        </a>
    </p>

    <!-- Formulaire de suppression -->
    <h2>Delete Enrollment</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/enrollments">
        <input type="hidden" name="_method" value="DELETE" />
        <input type="hidden" name="id" value="${enrollment.id}" />
        <button type="submit">Delete Enrollment</button>
    </form>

    <!-- Afficher les notes pour chaque assignment -->
    <h2>Grades for Assignments</h2>
    <c:if test="${not empty assignmentGrade}">
        <table border="1">
            <thead>
            <tr>
                <th>Assignment Title</th>
                <th>Description</th>
                <th>Grade</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="entry" items="${assignmentGrade}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/assignments/${entry.key.id}">
                                ${entry.key.title}
                        </a>
                    </td>
                    <td>${entry.key.description}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty entry.value}">
                                ${entry.value.score}
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
    </c:if>

    <c:if test="${empty assignmentGrade}">
        <p>No grades available for this enrollment.</p>
    </c:if>

    <!-- Statistiques des notes pour cet enrollment -->
    <h3>Grade Statistics for this Enrollment</h3>
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
                        <fmt:formatNumber value="${enrollmentStats.average}" maxFractionDigits="2"/>
                    </c:when>
                    <c:otherwise>
                        N/A
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${not empty enrollmentStats}">
                        <fmt:formatNumber value="${enrollmentStats.min}" maxFractionDigits="2"/>
                    </c:when>
                    <c:otherwise>
                        N/A
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${not empty enrollmentStats}">
                        <fmt:formatNumber value="${enrollmentStats.max}" maxFractionDigits="2"/>
                    </c:when>
                    <c:otherwise>
                        N/A
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        </tbody>
    </table>
</c:if>

<c:if test="${empty enrollment}">
    <p>Enrollment not found.</p>
</c:if>

</body>
</html>
