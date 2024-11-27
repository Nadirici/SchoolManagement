<%@ page import="fr.cyu.schoolmanagementsystem.model.dto.CourseDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.cyu.schoolmanagementsystem.model.dto.StudentDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tableau de bord</title>
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<div class="container">
  <%@include file="../headers/student_header.jsp"%>
  <div class="main-content">


    <div class="overviewStudent">
      <h2>Cours disponibles</h2>


      <%

        List<CourseDTO> availableCourses = (List<CourseDTO>) request.getAttribute("availableCourses");

        // Vérification que la liste des cours n'est pas vide
        if (availableCourses != null && !availableCourses.isEmpty()) {
      %>
      <table>
        <thead>
        <tr>

          <th>Cours</th>
          <th>Description du cours</th>
          <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
          // Boucle classique pour itérer sur les cours
          for (int i = 0; i < availableCourses.size(); i++) {
            CourseDTO course = availableCourses.get(i);
        %>
        <tr>

          <td><%= course.getName() %></td>
          <td><%= course.getDescription() %></td>
          <td>
            <form action="/students/${student.id}/courses/<%= course.getId() %> /enroll" method="post">
              <input type="submit" value="S'inscrire">
            </form>
          </td>
        </tr>
        <% } %> <!-- Fin de la boucle -->
        </tbody>
      </table>
      <%
      } else {
      %>
      <p>Pas de cours disponible pour le moment.</p>
      <%
        }
      %>

    </div>
  </div>
</div>
</body>
</html>
