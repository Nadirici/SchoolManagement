<%@ page import="servlets.schoolmanagement.models.entity.Student" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="c" %>
<%
    // Vérifiez si l'ID de l'étudiant commence par "1" côté JSP
    Student student = (Student) request.getAttribute("student");

%>


<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Tableau de bord - Étudiant</title>

</head>
<body>
<header>
    <h1>Bienvenue sur votre tableau de bord,  <%=student.getFirstName() +" "+student.getLastName() %></h1>
    <nav>
        <ul>
            <li><a href="<c:url value='/studentDashboard' />">Accueil</a></li>
            <li><a href="<c:url value='/studentCourses' />">Mes Cours</a></li>
            <li><a href="<c:url value='/studentProfile' />">Mon Profil</a></li>
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
    <p>Vous trouverez ici toutes les informations importantes et les annonces.</p>
    <!-- Ajoutez ici du contenu spécifique aux étudiants -->
</main>
</body>
</html>
