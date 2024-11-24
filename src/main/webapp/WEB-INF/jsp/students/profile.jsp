


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
  <title>${course.name}</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tableau de bord</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

  <%@include file="../headers/student_header.jsp"%>
  <div class="main-content">

    <div class="overviewStudent">
      <h1>Profil de l'étudiant</h1>



      <form action="${pageContext.request.contextPath}/students/${student.id}/profile" method="post">


        <label for="firstname">Prénom :</label>
        <input type="text" id="firstname" name="firstname" value="${student.firstname}" disabled/><br/>

        <label for="lastname">Nom :</label>
        <input type="text" id="lastname" name="lastname" value="${student.lastname}" disabled/><br/>

        <label for="email">Email :</label>
        <input type="email" id="email" name="email" value="${student.email}" /><br/>

        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" /><br/>

        <input type="submit" value="Mettre à jour le profil"/>
      </form>

      <a href="${pageContext.request.contextPath}/students/${student.id}">Retour à votre tableau de bord</a>
      </div>
      <%@ include file="../courses/course_details_table.jsp" %>


    </div>
  </div>
</div>
</body>
</html>
