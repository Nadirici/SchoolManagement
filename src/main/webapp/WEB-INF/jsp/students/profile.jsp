


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
  <title>${course.name}</title>
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
        <c:if test="${not empty flashMessage}">
            <div class="flash-message">
                <c:choose>
                    <c:when test="${flashMessage == 'UsedEmail'}">
                        <p class="flash-error">Cet email est déjà utilisé. Veuillez en choisir un autre.</p>
                    </c:when>
                    <c:when test="${flashMessage == 'TeacherUpdated'}">
                        <p class="flash-success">Votre profil a été mis à jour avec succès.</p>
                    </c:when>
                    <c:when test="${flashMessage == 'TeacherNotFound'}">
                        <p class="flash-warning">Enseignant introuvable. Veuillez réessayer.</p>
                    </c:when>
                    <c:otherwise>
                        <p class="flash-success">Modification réalisée avec succès</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <h1>Profil de l'étudiant</h1>

        <div class="form-container">
        <form action="${pageContext.request.contextPath}/students/${student.id}/profile" method="post">

        <label for="firstname">Prénom :</label>
        <input type="text" id="firstname" name="firstname" value="${student.firstname}" disabled/><br/>

        <label for="lastname">Nom :</label>
        <input type="text" id="lastname" name="lastname" value="${student.lastname}" disabled/><br/>

        <label for="email">Email :</label>
        <input type="email" id="email" name="email" value="${student.email}" /><br/>

        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" /><br/>

            <button class="float-right" type="submit" >Mettre à jour le profil</button>
      </form>
        </div>
      </div>
      <%@ include file="../courses/course_details_table.jsp" %>


    </div>
  </div>

</body>
</html>

