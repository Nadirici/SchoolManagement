<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<input type="text" id="searchInput" placeholder="Rechercher..." onkeyup="filterTable()">
<table border="1" id="myTable">
    <thead>
    <tr>

        <th>Cours</th>
        <th>Description du cours</th>
        <th>Horaire</th>
        <th>Enseignant</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="course" items="${courses}" varStatus="status">
        <tr>

            <td>${course.name}</td>
            <td>${course.description}</td>
            <td>${course.frenchDayOfWeek} de ${course.startTime} à ${course.endTime}</td>
            <td>${course.teacher.firstname} ${course.teacher.lastname}</td>
            <c:choose>

                <c:when test="${userType == 'student'}">
                    <td>
                        <div class="form-container">

                        <form action="/students/${student.id}/courses/${course.id}/enroll" method="post">
                            <input type="submit" value="Enroll">
                        </form>
                        </div>
                    </td>
                </c:when>
                <c:otherwise>
                    <td><a class="button" href="../${user.id}/courses/${course.id}" class="button">Voir plus</a></td>
                </c:otherwise>

            </c:choose>

        </tr></c:forEach>
    </tbody>
</table>
<script src="/js/search.js"></script>
