<table border="1">
    <thead>
    <tr>
        <th>#</th> <!-- Indice -->
        <th>Cours</th>
        <th>Description du cours</th>
        <th>Enseignant</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="course" items="${courses}" varStatus="status">
        <tr>
            <!-- Affichage de l'indice -->
            <td>${status.index + 1}</td> <!-- L'indice commence à 0, donc on ajoute 1 -->

            <td>${course.name}</td>
            <td>${course.description}</td>
            <td>${course.teacher.firstname} ${course.teacher.lastname}</td>
            <c:choose>

                <c:when test="${userType == 'student'}">
                    <td>
                        <form action="/students/${student.id}/courses/${course.id}/enroll" method="post">
                            <input type="submit" value="Enroll">
                        </form>
                    </td>
                </c:when>
                <c:otherwise>
                    <td><a href="../${user.id}/courses/${course.id}" class="button">Voir plus</a></td>
                </c:otherwise>

            </c:choose>

        </tr></c:forEach>
    </tbody>
</table>
