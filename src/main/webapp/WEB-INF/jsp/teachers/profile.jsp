<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Profil de l'enseignant</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

    <%@include file="../headers/teacher_header.jsp"%>
    <div class="main-content">
        <!-- Affichage des messages flash -->
        <%
            String flashMessage = (String) request.getAttribute("flashMessage");

            // Vérifiez si le flashMessage est nul et, dans ce cas, récupérez le paramètre de l'URL
            if (flashMessage == null) {
                flashMessage = request.getParameter("flashMessage");
            }


            if (flashMessage != null) {
                switch (flashMessage) {
                    case "UsedMail":
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


        <div class="overviewStudent">
            <h1>Profil de l'enseignant</h1>
            <div class="form-container">
            <form action="${pageContext.request.contextPath}/teachers/${teacher.id}/profile" method="post">


                <label for="firstname">Prénom :</label>
                <input type="text" id="firstname" name="firstname" value="${teacher.firstname}" disabled/><br/>

                <label for="lastname">Nom :</label>
                <input type="text" id="lastname" name="lastname" value="${teacher.lastname}" disabled/><br/>

                <label for="email">Email :</label>
                <input type="email" id="email" name="email" value="${teacher.email}" /><br/>

                <label for="password">Mot de passe :</label>
                <input type="password" id="password" name="password" /><br/>

                <input type="submit" value="Mettre à jour le profil"/>
            </form>
            </div>
            <br>
        </div>

        <%@ include file="../courses/course_details_table.jsp" %>

    </div>
</div>
</div>
</body>
</html>
