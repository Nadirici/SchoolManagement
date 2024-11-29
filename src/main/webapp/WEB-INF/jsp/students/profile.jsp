


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


        <h1>Profil de l'étudiant</h1>

        <%
            String flashMessage = (String) request.getAttribute("flashMessage");

            // Vérifiez si le flashMessage est nul et, dans ce cas, récupérez le paramètre de l'URL
            if (flashMessage == null) {
                flashMessage = request.getParameter("flashMessage");
            }


            if (flashMessage != null) {
                switch (flashMessage) {
                    case "UsedEmail":
                        out.println("<div class='flash-message flash-error'>Cet email est déjà utilisé. Veuillez en choisir un autre.</div>");
                        break;
                    case "TeacherUpdated":
                        out.println("<div class='flash-message flash-success'>Votre profil a été mis à jour avec succès.</div>");
                        break;
                    case "notValidMail":
                        out.println("<div class='flash-message flash-error'>Le format de l'email n'est pas valide.</div>");
                        break;

                    default:
                        out.println("<div class='flash-mesage flash-error'>Erreur inconnue.</div>");
                }
            }
        %>



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

