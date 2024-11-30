<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Course Details</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

  <%@ include file="../headers/admin_header.jsp" %> <!-- Assurez-vous que le fichier existe -->

  <div class="main-content">
    <div class="overviewStudent">
      <h2>${course.name}</h2>



      <!-- Statistiques supplémentaires -->
      <div class="stats">
        <div class="stat-card">
          <h3>Description :</h3>
          <p>${course.description}</p>
        </div>
        <div class="stat-card">
          <h3>Enseignant :</h3>
          <p>${course.teacher.firstname} ${course.teacher.lastname}</p>
        </div>
      </div>

      <%@ include file="../courses/course_details_table.jsp" %> <!-- Vérifiez que le fichier existe -->

    </div> <!-- Fermeture de .overviewStudent -->
  </div> <!-- Fermeture de .main-content -->
</div> <!-- Fermeture de .container -->

</body>
</html>
