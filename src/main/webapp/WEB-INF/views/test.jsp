<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <form action="/inscription/soumettreInscription" method="post">

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

            <button type="submit">Demander l'inscription</button>
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

            <div th:if="${message}" th:text="${message}"></div>
            <%-- Vérification de l'attribut errorMessage --%>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            </c:if>
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
