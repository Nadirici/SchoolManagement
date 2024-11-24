
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table border="1">
    <thead>
    <tr>
        <th>Prenom</th>
        <th>Nom</th>
        <th>Note</th>
    </tr>
    </thead>
    <tbody>

    <!-- Inclure la page liste.jsp -->
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.firstname}</td>
            <td>${student.lastname}</td>
            <td>
                <input type="number" step="0.1" name="grade_${student.id}" value="${studentGrades[student.id].score}" required />
            </td>

        </tr>
    </c:forEach>

    </tbody>
</table>
<div>
    <button type="submit">Enregistrer les Notes</button>
</div>
