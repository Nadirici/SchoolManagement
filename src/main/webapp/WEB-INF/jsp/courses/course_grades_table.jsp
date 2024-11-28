<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${course.name}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <c:if test="${teacher != null}">
        <%@include file="../headers/teacher_header.jsp" %>
    </c:if>
    <c:if test="${admin != null}">
        <%@include file="../headers/admin_header.jsp" %>
    </c:if>
        <div class="main-content">
            <div class="overviewStudent">
        <h2>Devoirs</h2>
        <c:if test="${canViewGrades}">
        <table border="1">
            <thead>
            <tr>
                <th>Prenom</th>
                <th>Nom</th>
                <c:forEach var="assignment" items="${assignments}">
                    <th>${assignment.title}</th>
                </c:forEach>
                <th>Moyenne</th>
            </tr>
            </thead>
            <tbody>

                <!-- Inclure la page liste.jsp -->
                <c:forEach var="student" items="${students}">
                    <tr>
                        <td>${student.firstname}</td>
                        <td>${student.lastname}</td>
                    <c:forEach var="assignment" items="${assignments}">
                        <td><c:out value="${assignmentsGrades[student.id][assignment.id]}" /></td>
                    </c:forEach>
                        <td><c:out value="${averageGrades[student.id]}" /></td>
                    </tr>
                </c:forEach>
                <a class="button" href="../${course.id}">Revenir au cours</a>



            </tbody>
        </table>
        </c:if>
            </div>
        </div>
</div>
</div>
</body>
</html>
