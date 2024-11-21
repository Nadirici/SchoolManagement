<%@ page import="fr.cyu.schoolmanagementsystem.model.dto.CourseDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.cyu.schoolmanagementsystem.model.dto.StudentDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Student Dashboard</title>
  <link rel="stylesheet" href="/css/style.css">
</head>



<body>
<div class="container">
  <div class="sidebar">
    <div class="dashboard-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--! Font Awesome Free --><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
    </div>
    <h3>Student Dashboard</h3>
    <ul>

      <% StudentDTO studentDTO = (StudentDTO) request.getAttribute("student"); %>
      <li><a href="/students/${student.id}" class="active">Dashboard</a></li>
      <li><a href="/students/<%= studentDTO.getId()%>/enroll">S'inscrire</a></li>
      <li><a href="/students/<%= studentDTO.getId()%>/profile">Profile</a></li>

      <li><a href="/logout">Se déconnecter</a></li>

    </ul>
  </div>

  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Bonjour ${student.firstname},</h1>
        <h2>Bienvenue sur ton <span>Dashboard!</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username"><%= studentDTO.getFirstname()%> <%= studentDTO.getLastname()%></span>
          <span class="user-email"><%= studentDTO.getEmail()%></span>
        </div>
      </div>
    </header>

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
          <th>#</th> <!-- Indice -->
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
          <td><%= i + 1 %></td> <!-- Affichage de l'indice -->
          <td><%= course.getName() %></td>
          <td><%= course.getDescription() %></td>
          <td>
            <form action="/students/${student.id}/courses/<%= course.getId() %> /enroll" method="post">
              <input type="submit" value="Enroll">
            </form>
          </td>
        </tr>
        <% } %> <!-- Fin de la boucle -->
        </tbody>
      </table>
      <%
      } else {
      %>
      <p>No courses available at the moment.</p>
      <%
        }
      %>

    </div>
  </div>
</div>
</body>
</html>
