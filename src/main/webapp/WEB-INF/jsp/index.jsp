

<%@page language="java" contentType="text/html; ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
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
    <link rel="stylesheet" type="text/css" href="/login.css">

</head>
<body>

<div class="container" id="container">
    <div class="form-container sign-up-container">

        <form action="${pageContext.request.contextPath}/api/register" method="post">
        <h1>Inscrivez vous</h1>
            <div class="infield">
            <select id="userType" name="userType" required>
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

            <!-- JavaScript pour faire afficher le departement a la place -->
            <div class="infield">
                <input type="date" placeholder="Votre date de naissance" id="date_of_birth" name="date_of_birth" required/>
                <label></label>
            </div>
            <div class="infield">
                <input type="password" placeholder="Mot de passe" name="password" required/>
                <label></label>
            </div>

            <button type="submit" onclick="return validateDate()">Demander l'inscription</button>
            <script>
                function validateDate() {
                    const dateInput = document.getElementById('date_of_birth');
                    const dateValue = dateInput.value;

                    // Vérifie si la valeur est au format YYYY-MM-DD
                    const regex = /^\d{4}-\d{2}-\d{2}$/;

                    if (!regex.test(dateValue)) {
                        alert("Veuillez entrer une date valide au format YYYY-MM-DD.");
                        return false; // Empêche l'envoi du formulaire
                    }
                    return true; // Permet l'envoi du formulaire
                }
            </script>
        </form>
    </div>
    <div class="form-container sign-in-container">
        <form action="#" method="get">
            <h1>Se connecter</h1>

            <div class="infield">
                <input type="email" placeholder="Email"  name="email" required/>
                <label></label>
            </div>
            <div class="infield">
                <input type="password" placeholder="Password" name="password" required />
                <label></label>
            </div>

            <% if (request.getAttribute("message") != null) { %>
            <div><%= request.getAttribute("message") %></div>
            <% } %>

            <%-- Vérification de l'attribut errorMessage --%>
            <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <p><%= request.getAttribute("errorMessage") %></p>
            </div>
            <% } %>
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




<script>

    const container = document.getElementById('container');
    const overlaycon = document.getElementById('overlayCon');
    const overlayBtn = document.getElementById('overlayBtn');

    overlayBtn.addEventListener('click',()=>{
        container.classList.toggle('right-panel-active');
        overlayBtn.classList.remove('btnScaled');
        window.requestAnimationFrame( ()=>{
            overlayBtn.classList.add('btnScaled');
        })
    });




</script>

</body>
</html>
