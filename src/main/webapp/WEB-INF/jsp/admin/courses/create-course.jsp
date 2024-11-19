<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Create a New Course</title>
  <link rel="stylesheet" href="/css/style.css">
  <script>

    document.addEventListener('DOMContentLoaded', function () {
      // Vérifier l'existence des éléments avant d'ajouter des écouteurs d'événements
      const teacherSelect = document.getElementById('teacherSelect');
      const submitButton = document.getElementById('submitButton');

      if (teacherSelect) {
        teacherSelect.addEventListener('change', validateForm);
      }

      if (submitButton) {
        submitButton.disabled = true;  // Désactiver le bouton par défaut
      }
    });

    // Fonction pour charger les enseignants dynamiquement selon le département
    function loadTeachers(department) {
      const teacherSelect = document.getElementById('teacherSelect');
      const submitButton = document.getElementById('submitButton');  // Assurez-vous que le bouton submit a cet ID
      teacherSelect.innerHTML = '';  // Réinitialiser la liste des enseignants

      // Désactiver le bouton de soumission par défaut
      submitButton.disabled = true;

      if (department) {
        console.log("Fetching teachers for department:", department);  // Vérifier la sélection du département

        // Effectuer une requête POST avec les données nécessaires
        fetch(`/admin/${admin.id}/teachers/` + department, {  // Correcte l'URL en incluant le département
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
        })
                .then(response => {
                  if (!response.ok) {
                    throw new Error('Network response was not ok');
                  }
                  return response.json();
                })
                .then(data => {
                  console.log("Data received from server:", data);  // Vérifier ce qui est renvoyé par le serveur

                  // Ajouter une option par défaut
                  const defaultOption = document.createElement('option');
                  defaultOption.value = '';
                  defaultOption.text = 'Selectionner un professeur de ce département';
                  teacherSelect.appendChild(defaultOption);

                  if (data.length === 0) {
                    console.log('No teachers found for this department');
                    // Si aucun professeur, modifier le message par défaut
                    defaultOption.text = 'Aucun professeur disponible pour ce département';
                  } else {
                    // Ajouter les enseignants comme options dans le select
                    data.forEach(teacher => {
                      const option = document.createElement('option');
                      option.value = teacher.email;  // Utilisez l'ID de l'enseignant pour la valeur
                      option.text = teacher.firstname + ' ' + teacher.lastname;  // Nom et prénom de l'enseignant
                      teacherSelect.appendChild(option);
                    });
                  }
                })
                .catch(error => {
                  console.error('Error fetching teachers:', error);  // Gestion des erreurs
                });
      }
    }

    // Fonction pour vérifier si tous les champs sont remplis
    function validateForm() {
      const teacherSelect = document.getElementById('teacherSelect');
      const submitButton = document.getElementById('submitButton');

      // Activer le bouton de soumission si tous les champs sont remplis
      if (teacherSelect.value && teacherSelect.value !== 'Aucun professeur disponible pour ce département') {
        submitButton.disabled = false;
      } else {
        submitButton.disabled = true;
      }
    }


  </script>



</head>
<body>
<div class="container">
  <div class="sidebar">
    <div class="dashboard-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
    </div>
    <h3>Admin Dashboard</h3>
    <h2>Overview</h2>
    <ul>
      <li><a href="/admin/${id}" class="active">Overview</a></li>
      <li><a href="/admin/${id}/students">Students</a></li>
      <li><a href="/admin/${id}/teachers">Teachers</a></li>
      <li><a href="/admin/${id}/courses">Courses</a></li>
      <li><a href="/admin/${id}/results">Results</a></li>
      <li><a href="/admin/${id}/requests">Demandes d'inscription</a></li>
      <form action="/logout" method="post">
        <li><input type="submit" value="Déconnexion"></li>
      </form>
    </ul>
  </div>

  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Hi ${admin.firstname},</h1>
        <h2>Welcome to <span>Courses Management Dashboard!</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username">${admin.firstname} ${admin.lastname}</span>
          <span class="user-email">${admin.email}</span>
        </div>
      </div>
    </header>

    <div class="overview">
      <h1>Create a New Course</h1>
      <form action="/admin/${admin.id}/courses/create" method="POST">
        <!-- Département -->
        <div>
          <label for="departmentSelect">Department:</label>
          <select id="departmentSelect" name="department" onchange="loadTeachers(this.value)">
            <option value="">Selectionner le departement</option>
            <!-- Remplir les départements dynamiquement -->
            <c:forEach var="department" items="${departments}">
              <option value="${department}">${department}</option>
            </c:forEach>
          </select>
        </div>

        <!-- Enseignant -->
        <div>
          <label for="teacherSelect">Teacher:</label>
          <select id="teacherSelect" name="teacher">
            <option value="">Select a teacher</option>
            <!-- Les enseignants seront ajoutés dynamiquement ici -->
          </select>
        </div>

        <!-- Nom du cours -->
        <div>
          <label for="courseName">Course Name:</label>
          <input type="text" id="courseName" name="courseName" required>
        </div>

        <!-- Description du cours -->
        <div>
          <label for="courseDescription">Course Description:</label>
          <textarea id="courseDescription" name="courseDescription" required></textarea>
        </div>

<%
    String flashMessage = (String) request.getAttribute("flashMessage");

    // Vérifiez si le flashMessage est nul et, dans ce cas, récupérez le paramètre de l'URL
    if (flashMessage == null) {
      flashMessage = request.getParameter("flashMessage");
    }
    if (flashMessage != null) {
      switch (flashMessage) {
        case "courseCreated":
          out.println("<div class='flash-message flash-error'>Cours crée avec succès !</div>");
          break;
        case "teacherNotfound":
          out.println("<div class='flash-message flash-error'>Professeur introuvable</div>");
        default:
          out.println("<div class='flash-mesage flash-error'>Erreur inconnue.</div>");
      }
    }
%>

        <!-- Bouton pour créer le cours -->
        <button id="submitButton" type="submit">Create Course</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>
