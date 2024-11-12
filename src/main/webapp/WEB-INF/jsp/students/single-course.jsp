<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 06/11/2024
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${course.name}</title>
</head>
<body>
    <h1>${course.name}</h1>
    <p>Description : ${course.description}</p>
    <p>Enseignant : ${course.teacher.firstname} ${course.teacher.lastname}</p>
    <div>
        <h2>Devoirs</h2>
        <c:if test="${canViewDetails}">
            <table border="1">
                <thead>
                <tr>
                    <th>Nom de l'évaluation</th>
                    <th>Coefficient</th>
                    <c:choose>
                        <c:when test="${isEnrolledStudent}">
                            <th>Note</th>
                        </c:when>
                        <c:otherwise>
                            <th>Note minimale</th>
                            <th>Note maximale</th>
                        </c:otherwise>
                    </c:choose>

                    <th>Moyenne de la classe</th>
                </tr>
                </thead>
                <tbody>

                <!-- Inclure la page liste.jsp -->
                <c:forEach var="assignment" items="${assignments}">
                    <tr>
                        <td>${assignment.title}</td>
                        <td>${assignment.coefficient}</td>
                        <c:choose>
                            <c:when test="${isEnrolledStudent}">
                                <td><c:out value="${studentAssignmentGrades[assignment.id]}" /></td>
                            </c:when>
                            <c:otherwise>
                                <td><c:out value="${minGrade[assignment.id]}" /></td>
                                <td><c:out value="${maxGrade[assignment.id]}" /></td>
                            </c:otherwise>
                        </c:choose>

                        <td><c:out value="${averageGrades[assignment.id]}" /></td>
                    </tr>
                </c:forEach>
                <!-- Ligne pour la moyenne générale
            <tr>
                <th>Moyenne générale</th>
                <td><%--${generalAverage}--%></td>
                <td><%--${generalMinScore}--%></td>
                <td><%--${generalMaxScore}--%></td>
                <td><%--${generalClassAverage}--%></td>
            </tr>-->
                </tbody>
            </table>
            <c:if test="${!isEnrolledStudent}">
                <a href="../courses/${course.id}/grades">Voir les notes </a>
            </c:if>
            <c:if test="${isAssignedTeacher}">
                <button onclick="window.location.href='${course.id}/add-assignment'">+ Ajouter un devoir</button>
            </c:if>
        </c:if>
    </div>
</body>
</html>
