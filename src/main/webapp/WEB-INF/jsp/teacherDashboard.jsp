<%@ page import="servlets.schoolmanagement.models.entity.Teacher" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="c"%>

<%
    // Vérifiez si l'ID de l'étudiant commence par "1" côté JSP
    Teacher teacher = (Teacher) request.getAttribute("teacher");

%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Tableau de bord - Enseignant</title>

</head>
<body>
<header>
    <h1>Bienvenue sur votre tableau de bord, <%= teacher.getFirstName() +" "+ teacher.getLastName()%>></h1>
    <nav>
        <ul>
            <li><a href="<c:url value='/teacherDashboard' />">Accueil</a></li>
            <li><a href="<c:url value='/teacherCourses' />">Mes Cours</a></li>
            <li><a href="<c:url value='/teacherProfile' />">Mon Profil</a></li>
            <li>
                <form action="/logout" method="post" style="display:inline;">
                <button type="submit">Se Déconnecter</button>
                </form>
            </li>
        </ul>
    </nav>
</header>
<main>
    <h2>Informations et Annonces</h2>
    <p>Vous trouverez ici les informations et les annonces concernant vos cours.</p>
    <!-- Ajoutez ici du contenu spécifique aux enseignants -->
</main>
</body>
</html>
