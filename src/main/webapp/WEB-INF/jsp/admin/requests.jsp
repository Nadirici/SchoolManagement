<%@ page import="servlets.schoolmanagement.models.entity.RegistrationRequest" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Demandes d'inscription</title>
    <link rel="stylesheet" type="text/css" href="<your-css-file.css>"> <!-- Lien vers votre fichier CSS -->
</head>
<body>
<h1>Demandes d'inscription en attente</h1>

<!-- Affichage d'un message si disponible -->
<% if (request.getAttribute("message") != null) { %>
<div><%= request.getAttribute("message") %></div>
<% } %>

<h2>Demandes des Professeurs</h2>
<table border="1">
    <%
    List<RegistrationRequest> pendingTeacherRequests = (List<RegistrationRequest>) request.getAttribute("pendingTeacherRequests");
    if (pendingTeacherRequests != null) {

%>
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Nom</th>
        <th>Prénom</th>
        <th>Actions</th>
    </tr>
    <%  for (RegistrationRequest teacherRequest : pendingTeacherRequests) { // Changer le nom ici %>

    <tr>
        <td><%= teacherRequest.getId() %></td>
        <td><%= teacherRequest.getTeacher().getEmail() %></td> <!-- Utilisation de la méthode pour obtenir l'email -->
        <td><%= teacherRequest.getTeacher().getLastName() %></td> <!-- Utilisation de la méthode pour obtenir le nom -->
        <td><%= teacherRequest.getTeacher().getFirstName() %></td> <!-- Utilisation de la méthode pour obtenir le prénom -->
        <td>
            <form action="/admin/approve/<%= teacherRequest.getId() %>" method="post" style="display:inline;">
                <button type="submit">Approuver</button>
            </form>
            <form action="/admin/reject/<%= teacherRequest.getId() %>" method="post" style="display:inline;">
                <button type="submit">Rejeter</button>
            </form>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5">Aucune demande en attente.</td>
    </tr>
    <%
        }
    %>
</table>

<h2>Demandes des Étudiants</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Nom</th>
        <th>Prénom</th>
        <th>Actions</th>
    </tr>
    <%
        List<RegistrationRequest> pendingStudentRequests = (List<RegistrationRequest>) request.getAttribute("pendingStudentRequests");
        if (pendingStudentRequests != null) {
            for (RegistrationRequest studentRequest : pendingStudentRequests) { // Changer le nom ici aussi
    %>
    <tr>
        <td><%= studentRequest.getId() %></td>
        <td><%= studentRequest.getStudent().getEmail() %></td> <!-- Utilisation de la méthode pour obtenir l'email -->
        <td><%= studentRequest.getStudent().getLastName() %></td> <!-- Utilisation de la méthode pour obtenir le nom -->
        <td><%= studentRequest.getStudent().getFirstName() %></td> <!-- Utilisation de la méthode pour obtenir le prénom -->
        <td>
            <form action="/admin/approve/<%= studentRequest.getId() %>" method="post" style="display:inline;">
                <button type="submit">Approuver</button>
            </form>
            <form action="/admin/reject/<%= studentRequest.getId() %>" method="post" style="display:inline;">
                <button type="submit">Rejeter</button>
            </form>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5">Aucune demande en attente.</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
