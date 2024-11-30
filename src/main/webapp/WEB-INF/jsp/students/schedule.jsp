<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Emploi du Temps de l'Étudiant</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<h1>Emploi du Temps de l'Étudiant</h1>

<table border="1" style="width: 100%; text-align: center;">
  <thead>
  <tr>
    <th>Heure</th>
    <!-- Itération sur les jours de la semaine (les clés de la Map) -->
    <c:forEach var="day" items="${studentSchedule}">
      <th>${day.key}</th> <!-- Afficher le jour de la semaine -->
    </c:forEach>
  </tr>
  </thead>
  <tbody>
  <!-- Itération sur les heures de la journée -->
  <c:forEach var="hour" items="${hoursOfDay}">
    <tr>
      <!-- Heure dans la colonne de gauche -->
      <td>${hour} </td>

      <!-- Itération sur les jours de la semaine pour afficher les cours -->
      <c:forEach var="dayEntry" items="${studentSchedule}">
        <td>
          <!-- Afficher les cours pour chaque jour et chaque heure -->
          <c:forEach var="course" items="${dayEntry.value}">
            <!-- Comparer l'heure du cours avec l'heure courante -->
            <c:if test="${course.startTime == hour}">
              <p>
                <strong>${course.name}</strong><br>
                  ${course.startTime} - ${course.endTime}
              </p>
            </c:if>
          </c:forEach>
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
  </tbody>
</table>

</body>
</html>