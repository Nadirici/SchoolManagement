<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Assignment Details</title>
</head>
<body>

<p>
    <a href="${pageContext.request.contextPath}/admin/assignments">Back to Assignments List</a>
</p>

<h1>Assignment Details</h1>

<!-- Vérifie si l'assignement est présent -->
<c:if test="${not empty assignment}">
    <p><strong>ID:</strong> ${assignment.id}</p>
    <p><strong>Title:</strong> ${assignment.title}</p>
    <p><strong>Description:</strong> ${assignment.description}</p>
    <p><strong>Coefficient:</strong> ${assignment.coefficient}</p>
    <p><strong>Course:</strong><a href="${pageContext.request.contextPath}/admin/courses?action=view&id=${assignment.course.id}">${assignment.course.name}</a></p>

    <!-- Formulaire de suppression -->
    <h2>Delete Assignment</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/assignments">
        <input type="hidden" name="_method" value="DELETE" />
        <input type="hidden" name="id" value="${assignment.id}" />
        <button type="submit">Delete Assignment</button>
    </form>

    <!-- Liste des étudiants inscrits à cet assignment avec leurs notes -->
    <h2>Enrolled Students and Grades</h2>
    <c:if test="${not empty enrollmentGrade}">
        <form action="${pageContext.request.contextPath}/admin/assignments" method="post">
            <input type="hidden" name="_method" value="PUT">
            <input type="hidden" name="action" value="saveGrades">
            <table border="1">
                <thead>
                <tr>
                    <th>Student Name</th>
                    <th>Grade</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="entry" items="${enrollmentGrade}">
                    <tr>
                        <td>${entry.key.student.firstname} ${entry.key.student.lastname}</td> <!-- Student Name -->
                        <td>
                            <input
                                    type="number"
                                    name="grades[${entry.value.id}]"
                                    value="${entry.value.score}"
                                    min="0"
                                    max="20"
                                    step="0.01"
                                    required />
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/enrollments?view=action&id=${entry.key.id}">View Enrollment Details</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <button type="submit">Save</button>
        </form>
    </c:if>
    <c:if test="${empty enrollmentGrade}">
        <p>No students enrolled in this assignment.</p>
    </c:if>

    <!-- Statistiques des notes des étudiants -->
        <h3>Grade Statistics for this Assignment</h3>
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
                        <c:when test="${not empty assignmentStats}">
                            <fmt:formatNumber value="${assignmentStats.average}" maxFractionDigits="2"/>
                        </c:when>
                        <c:otherwise>
                            N/A
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty assignmentStats}">
                            <fmt:formatNumber value="${assignmentStats.min}" maxFractionDigits="2"/>
                        </c:when>
                        <c:otherwise>
                            N/A
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty assignmentStats}">
                            <fmt:formatNumber value="${assignmentStats.max}" maxFractionDigits="2"/>
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

<c:if test="${empty assignment}">
    <p>Assignment not found.</p>
</c:if>

</body>
</html>
