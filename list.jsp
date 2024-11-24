<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List of Students</title>
    <style>
        /* Ajoutez quelques styles pour améliorer l'affichage */
        #searchInput {
            margin-bottom: 10px;
            padding: 5px;
            width: 100%;
            max-width: 300px;
        }
    </style>
</head>
<body>
<h1>List of Students </h1>

<c:if test="${not empty message}">
    <div class="message">${message}</div>
</c:if>
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<!-- Barre de recherche -->
<input type="text" id="searchInput" placeholder="Rechercher..." onkeyup="filterTable()">

<table border="1" id="myTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.id}</td>
            <td>${student.firstname}</td>
            <td>${student.lastname}</td>
            <td>${student.email}</td>
            <td>
                <a href="students/${student.id}">Admin View</a> |
                <a href="/students/${student.id}">* Student View</a> |
                <a href="students/${student.id}/courses">Courses</a> |
                <form action="students/${student.id}" method="post" style="display:inline;">
                    <input type="hidden" name="_method" value="delete"/>
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<a href="students/new">Add New Student</a>
<p>* Student View | Temporaire, le temps que le formulaire de connexion soit implémenté</p>

<script>
    function filterTable() {
        // Récupérer la valeur de la recherche
        const input = document.getElementById('searchInput');
        const filter = input.value.toLowerCase();
        const table = document.getElementById('myTable');
        const rows = table.getElementsByTagName('tr'); // Récupérer toutes les lignes du tableau

        // Parcourir toutes les lignes du tableau et les filtrer
        for (let i = 1; i < rows.length; i++) { // Commencer à 1 pour ignorer l'en-tête du tableau
            const cells = rows[i].getElementsByTagName('td');
            let match = false;

            // Vérifier si l'une des cellules de la ligne contient le texte de recherche
            for (let j = 0; j < cells.length; j++) {
                const cell = cells[j];
                if (cell.textContent.toLowerCase().includes(filter)) {
                    match = true;
                    break;
                }
            }

            // Afficher ou cacher la ligne en fonction de la recherche
            if (match) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
    }
</script>

</body>
</html>
