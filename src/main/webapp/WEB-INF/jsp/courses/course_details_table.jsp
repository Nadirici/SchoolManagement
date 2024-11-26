
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <div>
    <c:if test="${canViewDetails}">
      <h2>Devoirs</h2>

        <c:choose>
          <c:when test="${empty assignments}">
            <p>Il n'y a pas encore eu d'évaluation.</p>
          </c:when>
          <c:otherwise>
            <table border="1">
              <thead>
              <tr>
                <th>Nom de l'évaluation</th>
                <th>Coefficient</th>
                <th>Note minimale</th>
                <th>Note maximale</th>
                <th>Moyenne de la classe</th>
                <c:choose>
                    <c:when test="${isEnrolledStudent}">
                        <th>Note de l'étudiant</th>
                    </c:when>
                  <c:when test="${isAssignedTeacher}">
                      <th>Action</th>
                  </c:when>
                  <c:otherwise>

                  </c:otherwise>
                </c:choose>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="assignment" items="${assignments}">
                <tr>
                  <td>${assignment.title}</td>
                  <td>${assignment.coefficient}</td>
                  <td><c:out value="${minGrade[assignment.id]}" /></td>
                  <td><c:out value="${maxGrade[assignment.id]}" /></td>
                  <td><c:out value="${averageGrades[assignment.id]}" /></td>
                  <c:choose>
                    <c:when test="${isEnrolledStudent}">
                      <!-- Afficher la note de l'étudiant ou un message si la note est absente -->
                      <td>
                        <c:if test="${empty studentAssignmentGrades[assignment.id]}">
                          Tu n'as pas de note
                        </c:if>
                        <c:if test="${not empty studentAssignmentGrades[assignment.id]}">
                          <c:out value="${studentAssignmentGrades[assignment.id]}" />
                        </c:if>
                      </td>
                    </c:when>
                    <c:when test="${isAssignedTeacher}">
                      <td>
                        <form action="../courses/${course.id}/assignment/${assignment.id}" method="post" style="display:inline;">
                        <input type="hidden" name="_method" value="delete"/>
                        <button type="submit">Delete</button>
                        </form>
                        <a href="../courses/${course.id}/assignment/${assignment.id}" class="button">Update</a>
                      </td>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                  </c:choose>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </c:otherwise>
        </c:choose>
        <c:if test="${!isEnrolledStudent}">
          <a href="../courses/${course.id}/grades">Voir les notes</a>
        </c:if>
        <c:if test="${isAssignedTeacher}">
          <button onclick="window.location.href='../courses/${course.id}/add-assignment'">+ Ajouter un devoir</button>
        </c:if>
      </c:if>
  </div>