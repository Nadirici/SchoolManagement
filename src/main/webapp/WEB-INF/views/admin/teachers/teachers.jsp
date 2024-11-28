<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des enseignants | Tableau de bord Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Tableau de bord Admin</h3>
        <h2>Enseignants</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin">Aperçu</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/students">Étudiants</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/teachers" class="active">Enseignants</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/courses" >Cours</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/requests">Demandes d'inscription</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Bonjour ${admin.firstname},</h1>
                <h2>Bienvenue sur votre <span>Tableau de bord Admin !</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${admin.firstname} ${admin.lastname}</span>
                    <span class="user-email">${admin.email}</span>
                </div>
            </div>
        </header>

        <div class="overviewStudent">
            <h1>Gestion des Enseignants</h1>

            <!-- List of students -->
            <h2>Liste des enseignants vérifiés</h2>
            <!-- Barre de recherche -->
            <input type="text" id="searchInput" placeholder="Rechercher..." onkeyup="filterTable()">
            <c:if test="${not empty teachers}">
                <table border="1" id="myTable">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Prénom</th>
                        <th>Nom</th>
                        <th>Département</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="teacher" items="${teachers}">
                        <tr>
                            <td>${teacher.id}</td>
                            <td>${teacher.firstname}</td>
                            <td>${teacher.lastname}</td>
                            <td>${teacher.department}</td>
                            <td>${teacher.email}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/teachers/${teacher.id}">Détails</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty teachers}">
                <p>Aucun enseignant trouvé.</p>
            </c:if>

            <!-- Form to add a new student -->
            <h2>Ajouter un nouvel enseignant</h2>
            <form method="post" action="${pageContext.request.contextPath}/admin/teachers">
                <label for="firstname">Prénom :</label>
                <input type="text" id="firstname" name="firstname" required /><br />

                <label for="lastname">Nom :</label>
                <input type="text" id="lastname" name="lastname" required /><br />

                <label for="department">Département :</label>
                <select id="department" name="department">
                    <option value="" disabled selected>-- Choisissez votre departement --</option>
                    <option value="INFORMATIQUE">Informatique</option>
                    <option value="MATHEMATIQUES">Mathematiques</option>
                    <option value="PHYSIQUE">Physique</option>
                    <option value="CHIMIE">Chimie</option>
                </select><br />

                <label for="email">Email :</label>
                <input type="email" id="email" name="email" required /><br />

                <label for="password">Mot de passe :</label>
                <input type="password" id="password" name="password" required /><br />

                <button type="submit">Ajouter Enseignant</button>
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/search.js"></script>
</body>
</html>
