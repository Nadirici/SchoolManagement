<%@ page import="java.util.UUID" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Se connecter || S'inscrire</title>
    <!-- font awesome icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <!-- css stylesheet -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css?v=1.0">



</head>
<body>

<%-- Vérifier si l'utilisateur est déjà authentifié --%>
<%
    Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
    if (isAuthenticated != null && isAuthenticated) {
        String userType = (String) session.getAttribute("userType");
        UUID userId = (UUID) session.getAttribute("userId");
        // Rediriger selon le type d'utilisateur
        if (userType != null && userId != null) {
            switch (userType) {
                case "teacher":
                    response.sendRedirect(request.getContextPath() + "/teachers");
                    return;
                case "student":
                    response.sendRedirect(request.getContextPath() + "/students");
                    return;
                case "admin":
                    response.sendRedirect(request.getContextPath() + "/admin");
                    return;
                default:
                    break;
            }
        }
    }
%>



<div class="container" id="container">
    <div class="form-container sign-up-container">

        <form action="${pageContext.request.contextPath}/register" method="post">
            <h1>Inscrivez vous</h1>
            <div class="infield">
                <select id="userType" name="userType" required onchange="toggleDepartmentField()">
                    <option value="" disabled selected>-- Choisissez --</option>
                    <option value="student">Étudiant</option>
                    <option value="teacher">Professeur</option>
                </select>
            </div>
            <div class="infield">
                <input type="text" placeholder="Email" id="email" name="email" required/>
                <label></label>
            </div>
            <div class="infield">
                <input type="text" placeholder="Nom" id="lastname" name="lastname" required/>
                <label></label>
            </div>
            <div class="infield">
                <input type="text" placeholder="Prénom" id="firstname" name="firstname" required/>
                <label></label>
            </div>

            <!-- Champ de date de naissance -->
            <div class="infield" id="birthDateField">
                <input type="date" placeholder="Votre date de naissance" id="dateOfBirth" name="dateOfBirth" required/>
                <label></label>
            </div>

            <!-- S�lecteur de d�partement cach� par d�faut -->
            <div class="infield" id="departmentField" style="display: none;">
                <select id="department" name="department">
                    <option value="" disabled selected>-- Choisissez votre département --</option>
                    <option value="INFORMATIQUE">INFORMATIQUE</option>
                    <option value="MATHEMATIQUES">MATHEMATIQUES</option>
                    <option value="PHYSIQUE">PHYSIQUE</option>
                    <option value="CHIMIE">CHIMIE</option>

                </select>
            </div>

            <div class="infield">
                <input type="password" placeholder="Mot de passe" name="password" required/>
                <label></label>
            </div>

            <button type="submit">Demander l'inscription</button>

        </form>
    </div>
    <div class="form-container sign-in-container">
        <form action="${pageContext.request.contextPath}/login" method="post">
            <h1>Se connecter</h1>

            <div class="infield">
                <input type="email" placeholder="Email"  name="email" required/>
                <label></label>
            </div>
            <div class="infield">
                <input type="password" placeholder="Password" name="password" required />
                <label></label>
            </div>


            <!-- Affichage des messages flash -->
            <%
                String flashMessage = (String) request.getAttribute("flashMessage");
                if (flashMessage == null) {
                    flashMessage = request.getParameter("flashMessage");
                }
                // Afficher la valeur de flashMessage pour débogage
                if (flashMessage != null) {
                    switch (flashMessage) {
                        case "unauthenticated":
                            out.println("<div class='flash-message flash-error'>Vous devez être connecté pour accéder à cette page.</div>");
                            break;
                        case "unVerifiedStudent":
                            out.println("<div class='flash-message flash-error'>Votre compte étudiant n'est pas encore vérifié.</div>");
                            break;
                        case "unVerifiedTeacher":
                            out.println("<div class='flash-message flash-error'>Votre compte enseignant n'est pas vérifié.</div>");
                            break;
                        case "incorrectPassword":
                            out.println("<div class='flash-message flash-error'>Mot de passe incorrect.</div>");
                            break;
                        case "incorrectEmail":
                            out.println("<div class='flash-message flash-error'>Email incorrect.</div>");
                            break;
                        case "incorrectEmailOrPassword":
                            out.println("<div class='flash-message flash-error'>Email ou mot de passe incorrect.</div>");
                            break;
                        case "generalError":
                            out.println("<div class='flash-message flash-error'>Utilisateur inexistant, veuillez créer un compte.</div>");
                            break;
                        case "emailAlreadyExists":
                            out.println("<div class='flash-message flash-error'>Un utilisateur avec cet e-mail existe déjà.</div>");
                            break;
                        case "invalidUserType":
                            out.println("<div class='flash-message flash-error'>Type d'utilisateur invalide.</div>");
                            break;
                        case "registrationError":
                            out.println("<div class='flash-message flash-error'>Erreur lors de la soumission de la demande d'inscription.</div>");
                            break;
                        case "studentRequestSubmitted":
                            out.println("<div class='flash-message flash-success'>Demande d'inscription étudiant soumise avec succès !</div>");
                            break;
                        case "teacherRequestSubmitted":
                            out.println("<div class='flash-message flash-success'>Demande d'inscription professeur soumise avec succès !</div>");
                            break;
                        case "invalidEmail":
                            out.println("<div class='flash-message flash-error'>Format de l'email incorrect.</div>");
                            break;
                        case "invalidFirstname":
                            out.println("<div class='flash-message flash-error'>Format du prénom incorrect.</div>");
                            break;
                        case "invalidLastname":
                            out.println("<div class='flash-message flash-error'>Format du nom incorrect.</div>");
                            break;

                        case "invalidBirthDate":
                            out.println("<div class='flash-message flash-error'>Date de naissance incorrect.</div>");
                            break;
                        default:
                            out.println("<div class='flash-message flash-error'>Erreur inconnue.</div>");
                    }
                }
            %>

            <button >Se connecter</button>
        </form>
    </div>
    <div class="overlay-container" id="overlayCon">
        <div class="overlay">
            <div class="overlay-panel overlay-left">
                <h1>Se connecter</h1>
                <p>Pour vous connectez, renseignez vos information de connexion</p>
                <button>Se connecter</button>
            </div>
            <div class="overlay-panel overlay-right">
                <h1>Bonjour !</h1>
                <p>Renseignez vos informations pour vous inscrire</p>
                <button>S'inscrire</button>
            </div>
        </div>
        <button id="overlayBtn"></button>
    </div>

</div>








<script src="${pageContext.request.contextPath}/js/script.js"></script>

</body>
</html>