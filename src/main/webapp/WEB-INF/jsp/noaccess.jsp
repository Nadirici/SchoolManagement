<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Accès Refusé</title>
    <link rel="stylesheet" href="<c:url value='/css/noaccess.css' />">
    <style>
        /* Styles de base pour la page */
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f8f8f8;
        }
        .container {
            text-align: center;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ddd;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #e74c3c;
            margin-bottom: 10px;
        }
        p {
            margin: 0;
            color: #555;
        }
        a {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            color: #fff;
            background-color: #3498db;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Accès Refusé</h1>
    <!-- Affichage des messages flash -->
    <%
        // Récupérer les informations de session
        HttpSession sessionb = request.getSession(false);

        String flashError = (String) request.getAttribute("flashError");

        if (flashError != null) {
    %>
    <div class="flash-message flash-error"><%= flashError %></div>
    <%
        }


        String userType = (sessionb != null) ? (String) sessionb.getAttribute("userType") : null;

        if (userType != null) {
            // Vérifier le type d'utilisateur et afficher le lien vers le tableau de bord approprié
            if ("student".equals(userType)) {
    %>
    <p>Vous êtes un étudiant. Vous ne pouvez pas accéder à cette page. Retournez à votre tableau de bord.</p>
    <a href="/students/<%= sessionb.getAttribute("userId") %>">Retour à mon tableau de bord</a>
    <%
    } else if ("teacher".equals(userType)) {
    %>
    <p>Vous êtes un enseignant. Vous ne pouvez pas accéder à cette page. Retournez à votre tableau de bord.</p>
    <a href="/teachers/<%= sessionb.getAttribute("userId") %>">Retour à mon tableau de bord</a>
    <%
    } else if ("admin".equals(userType)) {
    %>
    <p>Vous êtes un administrateur. Vous ne pouvez pas accéder à cette page. Retournez à votre tableau de bord.</p>
    <a href="/admin/<%= sessionb.getAttribute("userId") %>">Retour à mon tableau de bord</a>
    <%
        }
    } else {
    %>
    <!-- Si l'utilisateur n'est pas connecté, l'inviter à se reconnecter -->
    <p>Vous n'êtes pas connecté. Veuillez vous reconnecter pour accéder à vos pages protégées.</p>
    <a href="/auth">Retour à la page de connexion</a>
    <%
        }
    %>
</div>
</body>
</html>
